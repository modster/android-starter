package com.example.empty_activity.ui.screentorch

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ScreenTorchScreenTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent { ScreenTorchScreen() }
  }

  @Test
  fun controls_exist() {
    composeTestRule.onNodeWithText("Screen torch").assertExists()
    composeTestRule.onNodeWithText("Brightness 100%").assertExists()
    composeTestRule.onNodeWithText("White").assertExists()
    composeTestRule.onNodeWithText("Warm bath").assertExists()
    composeTestRule.onNodeWithText("Romance").assertExists()
    composeTestRule.onNodeWithText("Aurora").assertExists()
    composeTestRule.onNodeWithText("Ember").assertExists()
  }
}
