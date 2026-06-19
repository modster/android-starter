package com.example.empty_activity.ui.screentorch

import android.view.KeyEvent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScreenTorchViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(ScreenTorchUiState())
  val uiState: StateFlow<ScreenTorchUiState> = _uiState.asStateFlow()

  fun setActive(active: Boolean) {
    _uiState.update { it.copy(isActive = active) }
  }

  fun setBrightness(brightness: Float) {
    _uiState.update { it.copy(brightness = brightness.coerceIn(MIN_SCREEN_BRIGHTNESS, MAX_SCREEN_BRIGHTNESS)) }
  }

  fun onVolumeKey(keyCode: Int): Boolean {
    if (!_uiState.value.isActive) return false

    val delta = when (keyCode) {
      KeyEvent.KEYCODE_VOLUME_UP -> SCREEN_BRIGHTNESS_STEP
      KeyEvent.KEYCODE_VOLUME_DOWN -> -SCREEN_BRIGHTNESS_STEP
      else -> return false
    }

    setBrightness(_uiState.value.brightness + delta)
    return true
  }
}

data class ScreenTorchUiState(
  val brightness: Float = MAX_SCREEN_BRIGHTNESS,
  val isActive: Boolean = false,
)

const val MIN_SCREEN_BRIGHTNESS = 0.05f
const val MAX_SCREEN_BRIGHTNESS = 1f
private const val SCREEN_BRIGHTNESS_STEP = 0.05f
