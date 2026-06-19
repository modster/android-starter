package com.example.empty_activity.ui.flashlight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.empty_activity.theme.EmptyActivityTheme
import kotlin.math.roundToInt

@Composable
fun FlashlightScreen(
  onScreenTorchClick: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: FlashlightViewModel,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  FlashlightScreen(
    state = state,
    onToggleClick = viewModel::toggleTorch,
    onBrightnessChange = viewModel::setBrightnessLevel,
    onScreenTorchClick = onScreenTorchClick,
    onErrorDismiss = viewModel::clearError,
    modifier = modifier,
  )
}

@Composable
internal fun FlashlightScreen(
  state: FlashlightUiState,
  onToggleClick: () -> Unit,
  onBrightnessChange: (Int) -> Unit,
  onScreenTorchClick: () -> Unit,
  onErrorDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val background = MaterialTheme.colorScheme.background
  val beamColor = if (state.isTorchOn) Color(0xFF1B1B1B) else Color(0xFF1B1B1B)
  val textColor = if (state.isTorchOn) Color(0xFFEDEDED) else Color(0xFFEDEDED)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(background),
  ) {
    Column(
      modifier = Modifier
        .align(Alignment.Center)
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
       Text(
          text = "Phlashlight",
        color = textColor,
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.Bold,
      )
      Text(
        text = if (state.isTorchOn) "Ad Phree." else "Ad Phree.",
        color = textColor.copy(alpha = 0.32f),
        textAlign = TextAlign.Center,
      )
      Spacer(Modifier.height(36.dp))
      Box(
        modifier = Modifier
          .size(150.dp)
          .clip(CircleShape)
          .background(beamColor)
          .border(2.dp, textColor.copy(alpha = 0.18f), CircleShape)
          .clickable(enabled = state.isTorchAvailable, onClick = onToggleClick),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = if (state.isTorchOn) "ON" else "OFF",
          color = textColor,
          fontSize = 34.sp,
          fontWeight = FontWeight.Black,
        )
      }
      Spacer(Modifier.height(36.dp))
      Button(onClick = onScreenTorchClick) {
        Text("Screen Torch")
      }
      Spacer(Modifier.height(36.dp))
      if (!state.isTorchAvailable) {
        Text(
          text = "No device torch found. Screen torch is still available.",
          color = textColor.copy(alpha = 0.28f),
          textAlign = TextAlign.Center,
        )
      } else if (state.supportsBrightness) {
        Text(
          text = "Brightness ${state.brightnessLevel}/${state.maxBrightnessLevel}",
          color = textColor.copy(alpha = 0.78f),
        )
        Slider(
          value = state.brightnessLevel.toFloat(),
          onValueChange = { onBrightnessChange(it.roundToInt()) },
          valueRange = 1f..state.maxBrightnessLevel.toFloat(),
          steps = (state.maxBrightnessLevel - 2).coerceAtLeast(0),
          modifier = Modifier.fillMaxWidth(),
        )
        Text(
          text = "Use volume buttons to adjust brightness.",
          color = textColor.copy(alpha = 0.62f),
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodySmall,
        )
      }
    }

    state.errorMessage?.let { message ->
      Snackbar(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(16.dp),
        action = {
          TextButton(onClick = onErrorDismiss) { Text("Dismiss") }
        },
      ) {
        Text(message)
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun FlashlightScreenPreview() {
  EmptyActivityTheme {
    FlashlightScreen(
      state = FlashlightUiState(
        isTorchAvailable = true,
        supportsBrightness = true,
        brightnessLevel = 4,
        maxBrightnessLevel = 10,
      ),
      onToggleClick = {},
      onBrightnessChange = {},
      onScreenTorchClick = {},
      onErrorDismiss = {},
    )
  }
}
