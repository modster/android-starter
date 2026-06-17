package com.example.empty_activity.ui.screentorch

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScreenTorchScreen(modifier: Modifier = Modifier) {
  val activity = LocalContext.current.findActivity()

  DisposableEffect(activity) {
    val window = activity?.window
    val previousBrightness = window?.attributes?.screenBrightness
    val hadKeepScreenOn = window
      ?.attributes
      ?.flags
      ?.and(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0

    if (window != null) {
      window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
      window.attributes = window.attributes.apply { screenBrightness = 1.0f }
    }

    onDispose {
      if (window != null && previousBrightness != null) {
        window.attributes = window.attributes.apply { screenBrightness = previousBrightness }
        if (hadKeepScreenOn == false) {
          window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
      }
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White),
    contentAlignment = Alignment.BottomCenter,
  ) {
    Text(text = "Back to exit", color = Color(0x22000000))
  }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
  is Activity -> this
  is ContextWrapper -> baseContext.findActivity()
  else -> null
}

@Preview(showBackground = true)
@Composable
fun ScreenTorchScreenPreview() {
  ScreenTorchScreen()
}
