package com.example.empty_activity.ui.flashlight

import android.view.KeyEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.empty_activity.torch.TorchController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlashlightViewModel(private val torchController: TorchController) : ViewModel() {
  private val _uiState = MutableStateFlow(
    FlashlightUiState(
      isTorchAvailable = torchController.isAvailable,
      maxBrightnessLevel = torchController.maxBrightnessLevel,
      supportsBrightness = torchController.supportsBrightness,
    ),
  )
  val uiState: StateFlow<FlashlightUiState> = _uiState.asStateFlow()

  init {
    torchController.registerCallback()
    viewModelScope.launch {
      combine(torchController.isTorchOn, torchController.brightnessLevel) { isTorchOn, brightnessLevel ->
        isTorchOn to brightnessLevel
      }.collect { (isTorchOn, brightnessLevel) ->
        _uiState.update {
          it.copy(
            isTorchOn = isTorchOn,
            brightnessLevel = brightnessLevel,
          )
        }
      }
    }
  }

  fun toggleTorch() {
    val state = _uiState.value
    if (!state.isTorchAvailable) {
      showError("This device does not report an available camera flash.")
      return
    }

    runCatching { torchController.setTorchEnabled(!state.isTorchOn) }
      .onFailure { showError(it.message ?: "Unable to toggle the torch.") }
  }

  fun setBrightnessLevel(level: Int) {
    val state = _uiState.value
    if (!state.supportsBrightness) return

    runCatching { torchController.setBrightnessLevel(level) }
      .onFailure { showError(it.message ?: "Unable to set torch brightness.") }
  }

  fun onVolumeKey(keyCode: Int): Boolean {
    val state = _uiState.value
    if (!state.isTorchOn || !state.supportsBrightness) return false

    val delta = when (keyCode) {
      KeyEvent.KEYCODE_VOLUME_UP -> 1
      KeyEvent.KEYCODE_VOLUME_DOWN -> -1
      else -> return false
    }

    setBrightnessLevel(state.brightnessLevel + delta)
    return true
  }

  fun clearError() {
    _uiState.update { it.copy(errorMessage = null) }
  }

  override fun onCleared() {
    runCatching { torchController.setTorchEnabled(false) }
    torchController.unregisterCallback()
    super.onCleared()
  }

  private fun showError(message: String) {
    _uiState.update { it.copy(errorMessage = message) }
  }
}

data class FlashlightUiState(
  val isTorchAvailable: Boolean = false,
  val isTorchOn: Boolean = false,
  val supportsBrightness: Boolean = false,
  val brightnessLevel: Int = 1,
  val maxBrightnessLevel: Int = 1,
  val errorMessage: String? = null,
)
