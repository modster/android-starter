package com.example.empty_activity.ui.screentorch

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun ScreenTorchScreen(
  modifier: Modifier = Modifier,
  viewModel: ScreenTorchViewModel = viewModel(),
) {
  var mode by rememberSaveable { mutableStateOf(ScreenTorchMode.White) }
  var controlsVisible by rememberSaveable { mutableStateOf(true) }
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val brightness = state.brightness
  val activity = LocalContext.current.findActivity()

  DisposableEffect(activity, viewModel) {
    val window = activity?.window
    val previousBrightness = window?.attributes?.screenBrightness
    val hadKeepScreenOn = window
      ?.attributes
      ?.flags
      ?.and(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0

    window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    onDispose {
      if (window != null && previousBrightness != null) {
        window.attributes = window.attributes.apply { screenBrightness = previousBrightness }
        if (hadKeepScreenOn == false) {
          window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
      }
      viewModel.setActive(false)
    }
  }

  LaunchedEffect(viewModel) {
    viewModel.setActive(true)
  }

  LaunchedEffect(activity, brightness) {
    activity?.window?.let { window ->
      window.attributes = window.attributes.apply { screenBrightness = brightness.coerceIn(MIN_SCREEN_BRIGHTNESS, MAX_SCREEN_BRIGHTNESS) }
    }
  }

  val interactionSource = remember { MutableInteractionSource() }
  Box(
    modifier = modifier
      .fillMaxSize()
      .clickable(
        interactionSource = interactionSource,
        indication = null,
      ) { controlsVisible = !controlsVisible },
  ) {
    MoodLightCanvas(mode = mode, modifier = Modifier.fillMaxSize())

    if (controlsVisible) {
      ScreenTorchControls(
        mode = mode,
        brightness = brightness,
        onModeChange = { mode = it },
        onBrightnessChange = viewModel::setBrightness,
        modifier = Modifier.align(Alignment.BottomCenter),
      )
    } else {
      Text(
        text = "Tap for controls",
        color = mode.contentColor.copy(alpha = 0.42f),
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .safeDrawingPadding()
          .padding(18.dp),
      )
    }
  }
}

@Composable
private fun MoodLightCanvas(mode: ScreenTorchMode, modifier: Modifier = Modifier) {
  val transition = rememberInfiniteTransition(label = "screen torch mood")
  val phase by transition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = mode.cycleMillis, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
    label = "mood phase",
  )

  Canvas(modifier = modifier) {
    when (mode) {
      ScreenTorchMode.White -> drawRect(Color.White)
      ScreenTorchMode.WarmBath -> drawMoodLight(
        phase = phase,
        baseColors = listOf(Color(0xFFFFD08A), Color(0xFFFF8A3D), Color(0xFF7A2E12)),
        firstGlow = Color(0xFFFFF0BA),
        secondGlow = Color(0xFFFF6D2D),
        thirdGlow = Color(0xFFA24420),
      )
      ScreenTorchMode.Candlelight -> drawCandlelight(phase)
      ScreenTorchMode.OceanDrift -> drawOceanDrift(phase)
      ScreenTorchMode.Romance -> drawMoodLight(
        phase = phase,
        baseColors = listOf(Color(0xFF341038), Color(0xFFC02D68), Color(0xFFFF7B9C)),
        firstGlow = Color(0xFFFFC1D5),
        secondGlow = Color(0xFFFF3D7F),
        thirdGlow = Color(0xFF7D35C9),
      )
      ScreenTorchMode.Aurora -> drawMoodLight(
        phase = phase,
        baseColors = listOf(Color(0xFF02111D), Color(0xFF0DAE8B), Color(0xFF6843C7), Color(0xFF2DCBFF)),
        firstGlow = Color(0xFF5EF7C8),
        secondGlow = Color(0xFF6256FF),
        thirdGlow = Color(0xFF1CB9FF),
      )
      ScreenTorchMode.LavaLamp -> drawLavaLamp(phase)
      ScreenTorchMode.Ember -> drawMoodLight(
        phase = phase,
        baseColors = listOf(Color(0xFF160807), Color(0xFF7E230C), Color(0xFFFFA44B)),
        firstGlow = Color(0xFFFFD078),
        secondGlow = Color(0xFFE74616),
        thirdGlow = Color(0xFFFF8D2A),
      )
    }
  }
}

private fun DrawScope.drawMoodLight(
  phase: Float,
  baseColors: List<Color>,
  firstGlow: Color,
  secondGlow: Color,
  thirdGlow: Color,
) {
  val angle = phase * TWO_PI
  val width = size.width
  val height = size.height
  val radius = max(width, height)

  drawRect(
    brush = Brush.linearGradient(
      colors = baseColors,
      start = Offset(width * (0.15f + 0.18f * sin(angle)), height * (0.08f + 0.12f * cos(angle))),
      end = Offset(width * (0.82f + 0.12f * cos(angle * 0.7f)), height * (0.95f + 0.08f * sin(angle * 0.6f))),
    ),
  )
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(firstGlow.copy(alpha = 0.92f), firstGlow.copy(alpha = 0.18f), Color.Transparent),
      center = Offset(width * (0.34f + 0.22f * sin(angle)), height * (0.32f + 0.18f * cos(angle * 1.2f))),
      radius = radius * 0.62f,
    ),
    radius = radius * 0.62f,
    center = Offset(width * (0.34f + 0.22f * sin(angle)), height * (0.32f + 0.18f * cos(angle * 1.2f))),
  )
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(secondGlow.copy(alpha = 0.7f), secondGlow.copy(alpha = 0.16f), Color.Transparent),
      center = Offset(width * (0.72f + 0.18f * cos(angle * 1.35f)), height * (0.56f + 0.22f * sin(angle * 0.9f))),
      radius = radius * 0.7f,
    ),
    radius = radius * 0.7f,
    center = Offset(width * (0.72f + 0.18f * cos(angle * 1.35f)), height * (0.56f + 0.22f * sin(angle * 0.9f))),
  )
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(thirdGlow.copy(alpha = 0.54f), Color.Transparent),
      center = Offset(width * (0.48f + 0.26f * sin(angle * 0.65f)), height * (0.82f + 0.10f * cos(angle * 1.6f))),
      radius = radius * 0.52f,
    ),
    radius = radius * 0.52f,
    center = Offset(width * (0.48f + 0.26f * sin(angle * 0.65f)), height * (0.82f + 0.10f * cos(angle * 1.6f))),
  )
  drawRect(Color.Black.copy(alpha = 0.08f))
}

private fun DrawScope.drawOceanDrift(phase: Float) {
  val angle = phase * TWO_PI
  val width = size.width
  val height = size.height
  val radius = max(width, height)

  drawRect(
    brush = Brush.linearGradient(
      colors = listOf(Color(0xFF00111F), Color(0xFF023A4A), Color(0xFF061B34), Color(0xFF0B5F6B)),
      start = Offset(width * (0.12f + 0.10f * sin(angle * 0.7f)), height * 0.10f),
      end = Offset(width * (0.88f + 0.08f * cos(angle * 0.8f)), height * 0.92f),
    ),
  )
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(Color(0xFF55F0FF).copy(alpha = 0.54f), Color(0xFF00A6B8).copy(alpha = 0.18f), Color.Transparent),
      center = Offset(width * (0.28f + 0.28f * sin(angle * 0.62f)), height * (0.44f + 0.10f * cos(angle))),
      radius = radius * 0.72f,
    ),
    radius = radius * 0.72f,
    center = Offset(width * (0.28f + 0.28f * sin(angle * 0.62f)), height * (0.44f + 0.10f * cos(angle))),
  )
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(Color(0xFF74FFD8).copy(alpha = 0.34f), Color.Transparent),
      center = Offset(width * (0.76f + 0.16f * cos(angle * 1.1f)), height * (0.66f + 0.18f * sin(angle * 0.72f))),
      radius = radius * 0.58f,
    ),
    radius = radius * 0.58f,
    center = Offset(width * (0.76f + 0.16f * cos(angle * 1.1f)), height * (0.66f + 0.18f * sin(angle * 0.72f))),
  )
  drawRect(
    brush = Brush.linearGradient(
      colors = listOf(Color.Transparent, Color(0xFF7CEBFF).copy(alpha = 0.14f), Color.Transparent),
      start = Offset(0f, height * (0.26f + 0.05f * sin(angle * 1.5f))),
      end = Offset(width, height * (0.54f + 0.05f * cos(angle * 1.2f))),
    ),
  )
}

private fun DrawScope.drawCandlelight(phase: Float) {
  val angle = phase * TWO_PI
  val width = size.width
  val height = size.height
  val radius = max(width, height)
  val flicker = (0.88f + 0.07f * sin(angle * 7f) + 0.05f * sin(angle * 13f)).coerceIn(0.76f, 1f)
  val center = Offset(width * (0.50f + 0.025f * sin(angle * 9f)), height * 0.66f)

  drawRect(
    brush = Brush.linearGradient(
      colors = listOf(Color(0xFF1B0802), Color(0xFF552006), Color(0xFF120503)),
      start = Offset(width * 0.5f, 0f),
      end = Offset(width * 0.5f, height),
    ),
  )
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(
        Color(0xFFFFF0B0).copy(alpha = 0.90f * flicker),
        Color(0xFFFFA13D).copy(alpha = 0.42f * flicker),
        Color(0xFFB54512).copy(alpha = 0.18f * flicker),
        Color.Transparent,
      ),
      center = center,
      radius = radius * (0.46f + 0.08f * flicker),
    ),
    radius = radius * (0.46f + 0.08f * flicker),
    center = center,
  )
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(Color(0xFFFFF9D7).copy(alpha = 0.62f * flicker), Color.Transparent),
      center = Offset(center.x, center.y - height * 0.15f),
      radius = radius * 0.18f * flicker,
    ),
    radius = radius * 0.18f * flicker,
    center = Offset(center.x, center.y - height * 0.15f),
  )
  drawRect(Color(0xFFFF6A1A).copy(alpha = 0.08f * flicker))
}

private fun DrawScope.drawLavaLamp(phase: Float) {
  val angle = phase * TWO_PI
  val width = size.width
  val height = size.height

  drawRect(
    brush = Brush.linearGradient(
      colors = listOf(Color(0xFF16051F), Color(0xFF3B0C32), Color(0xFF11041B)),
      start = Offset(width * 0.1f, 0f),
      end = Offset(width * 0.9f, height),
    ),
  )
  drawLavaBlob(Color(0xFFFF4F8B), angle, 0.30f, 0.30f, 0.30f, 0.16f, 0.22f, 0.78f, 1.12f)
  drawLavaBlob(Color(0xFFFF7A21), angle, 0.34f, 0.72f, 0.36f, 0.18f, 0.24f, 1.03f, 0.84f)
  drawLavaBlob(Color(0xFFB95CFF), angle, 0.28f, 0.48f, 0.70f, 0.22f, 0.16f, 0.68f, 1.26f)
  drawLavaBlob(Color(0xFFFF255F), angle, 0.24f, 0.78f, 0.78f, 0.12f, 0.18f, 1.36f, 0.64f)
  drawLavaBlob(Color(0xFFFFC15C), angle, 0.18f, 0.18f, 0.82f, 0.10f, 0.10f, 1.52f, 1.42f)
  drawRect(Color.Black.copy(alpha = 0.10f))
}

private fun DrawScope.drawLavaBlob(
  color: Color,
  angle: Float,
  radiusFraction: Float,
  baseX: Float,
  baseY: Float,
  xDrift: Float,
  yDrift: Float,
  xSpeed: Float,
  ySpeed: Float,
) {
  val radius = max(size.width, size.height) * radiusFraction
  val center = Offset(
    size.width * (baseX + xDrift * sin(angle * xSpeed)),
    size.height * (baseY + yDrift * cos(angle * ySpeed)),
  )

  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(color.copy(alpha = 0.82f), color.copy(alpha = 0.22f), Color.Transparent),
      center = center,
      radius = radius,
    ),
    radius = radius,
    center = center,
  )
}

@Composable
private fun ScreenTorchControls(
  mode: ScreenTorchMode,
  brightness: Float,
  onModeChange: (ScreenTorchMode) -> Unit,
  onBrightnessChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
      .background(Color.Black.copy(alpha = 0.50f))
      .safeDrawingPadding()
      .padding(horizontal = 20.dp, vertical = 18.dp),
  ) {
    Text(
      text = "Screen torch",
      color = Color.White,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
    )
    Text(
      text = "Tap the light to hide controls. Back exits.",
      color = Color.White.copy(alpha = 0.72f),
      style = MaterialTheme.typography.bodySmall,
    )
    Spacer(Modifier.height(14.dp))
    Row(
      modifier = Modifier.horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      ScreenTorchMode.entries.forEach { item ->
        if (item == mode) {
          Button(onClick = { onModeChange(item) }) { Text(item.label) }
        } else {
          TextButton(onClick = { onModeChange(item) }) {
            Text(text = item.label, color = Color.White)
          }
        }
      }
    }
    Spacer(Modifier.height(18.dp))
    Text(
      text = "Brightness ${(brightness * 100).roundToInt()}%",
      color = Color.White,
      style = MaterialTheme.typography.labelLarge,
      textAlign = TextAlign.Start,
    )
    Slider(
      value = brightness,
      onValueChange = onBrightnessChange,
      valueRange = MIN_SCREEN_BRIGHTNESS..MAX_SCREEN_BRIGHTNESS,
      modifier = Modifier.fillMaxWidth(),
    )
  }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
  is Activity -> this
  is ContextWrapper -> baseContext.findActivity()
  else -> null
}

private enum class ScreenTorchMode(
  val label: String,
  val contentColor: Color,
  val cycleMillis: Int,
) {
  White("White", Color(0xFF101010), 12_000),
  WarmBath("Warm bath", Color.White, 18_000),
  Candlelight("Candlelight", Color.White, 8_000),
  OceanDrift("Ocean drift", Color.White, 26_000),
  Romance("Romance", Color.White, 20_000),
  Aurora("Aurora", Color.White, 24_000),
  LavaLamp("Lava lamp", Color.White, 18_000),
  Ember("Ember", Color.White, 10_000),
}

private const val TWO_PI = (PI * 2).toFloat()

@Preview(showBackground = true)
@Composable
fun ScreenTorchScreenPreview() {
  ScreenTorchScreen()
}
