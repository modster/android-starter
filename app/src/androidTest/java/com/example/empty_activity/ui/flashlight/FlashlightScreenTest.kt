package com.example.empty_activity.ui.flashlight

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.empty_activity.theme.EmptyActivityTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FlashlightScreenTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent {
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
  }

  @Test
  fun flashlightControls_exist() {
    composeTestRule.onNodeWithText("Photon").assertExists()
    composeTestRule.onNodeWithText("OFF").assertExists()
    composeTestRule.onNodeWithText("Screen torch").assertExists()
    composeTestRule.onNodeWithText("Brightness 4/10").assertExists()
  }
}
