package com.example.empty_activity.torch

import android.annotation.TargetApi
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TorchController(context: Context) {
  private val cameraManager = context.getSystemService(CameraManager::class.java)
  private val mainHandler = Handler(Looper.getMainLooper())
  private val torchCameraId = findTorchCameraId()

  private val _isTorchOn = MutableStateFlow(false)
  val isTorchOn: StateFlow<Boolean> = _isTorchOn.asStateFlow()

  private val _brightnessLevel = MutableStateFlow(1)
  val brightnessLevel: StateFlow<Int> = _brightnessLevel.asStateFlow()

  val isAvailable: Boolean = torchCameraId != null
  val maxBrightnessLevel: Int = findMaxBrightnessLevel()
  val supportsBrightness: Boolean = maxBrightnessLevel > 1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

  private var callbackRegistered = false

  private val torchCallback = object : CameraManager.TorchCallback() {
    override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
      if (cameraId == torchCameraId) {
        _isTorchOn.value = enabled
      }
    }

    override fun onTorchModeUnavailable(cameraId: String) {
      if (cameraId == torchCameraId) {
        _isTorchOn.value = false
      }
    }

    override fun onTorchStrengthLevelChanged(cameraId: String, newStrengthLevel: Int) {
      if (cameraId == torchCameraId) {
        _brightnessLevel.value = newStrengthLevel.coerceIn(1, maxBrightnessLevel)
      }
    }
  }

  fun registerCallback() {
    if (!callbackRegistered) {
      cameraManager.registerTorchCallback(torchCallback, mainHandler)
      callbackRegistered = true
    }
  }

  fun unregisterCallback() {
    if (callbackRegistered) {
      cameraManager.unregisterTorchCallback(torchCallback)
      callbackRegistered = false
    }
  }

  fun setTorchEnabled(enabled: Boolean) {
    val cameraId = requireTorchCameraId()
    try {
      if (enabled && supportsBrightness) {
        turnOnTorchWithStrengthLevel(cameraId, _brightnessLevel.value.coerceIn(1, maxBrightnessLevel))
      } else {
        cameraManager.setTorchMode(cameraId, enabled)
      }
      _isTorchOn.value = enabled
    } catch (exception: CameraAccessException) {
      throw IllegalStateException("Unable to access camera torch", exception)
    } catch (exception: SecurityException) {
      throw IllegalStateException("Camera permission is required to control the torch", exception)
    }
  }

  fun setBrightnessLevel(level: Int) {
    if (!supportsBrightness) return

    val cameraId = requireTorchCameraId()
    val clampedLevel = level.coerceIn(1, maxBrightnessLevel)
    try {
      turnOnTorchWithStrengthLevel(cameraId, clampedLevel)
      _brightnessLevel.value = clampedLevel
      _isTorchOn.value = true
    } catch (exception: CameraAccessException) {
      throw IllegalStateException("Unable to set torch brightness", exception)
    } catch (exception: SecurityException) {
      throw IllegalStateException("Camera permission is required to set torch brightness", exception)
    }
  }

  private fun requireTorchCameraId(): String = torchCameraId ?: throw IllegalStateException("No torch is available")

  private fun findTorchCameraId(): String? {
    return try {
      cameraManager.cameraIdList.firstOrNull { cameraId ->
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        val isBackFacing = characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK
        hasFlash && isBackFacing
      } ?: cameraManager.cameraIdList.firstOrNull { cameraId ->
        cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
      }
    } catch (exception: CameraAccessException) {
      null
    }
  }

  private fun findMaxBrightnessLevel(): Int {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return 1

    val cameraId = torchCameraId ?: return 1
    return try {
      cameraManager
        .getCameraCharacteristics(cameraId)
        .get(CameraCharacteristics.FLASH_INFO_STRENGTH_MAXIMUM_LEVEL)
        ?.coerceAtLeast(1) ?: 1
    } catch (exception: CameraAccessException) {
      1
    }
  }

  @TargetApi(Build.VERSION_CODES.TIRAMISU)
  private fun turnOnTorchWithStrengthLevel(cameraId: String, level: Int) {
    cameraManager.turnOnTorchWithStrengthLevel(cameraId, level)
  }
}
