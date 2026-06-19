package com.example.empty_activity

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.empty_activity.ui.flashlight.FlashlightScreen
import com.example.empty_activity.ui.flashlight.FlashlightViewModel
import com.example.empty_activity.ui.screentorch.ScreenTorchScreen
import com.example.empty_activity.ui.screentorch.ScreenTorchViewModel

@Composable fun MainNavigation(
    flashlightViewModel: FlashlightViewModel,
    screenTorchViewModel: ScreenTorchViewModel,
)
{
    val backStack = rememberNavBackStack(Main)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Main> {
                FlashlightScreen(
                    onScreenTorchClick = { backStack.add(ScreenTorch) },
                    viewModel = flashlightViewModel,
                    modifier = Modifier
                        .safeDrawingPadding()
                        .padding(16.dp)
                )
            }
            entry<ScreenTorch> {
                ScreenTorchScreen(viewModel = screenTorchViewModel)
            }
        },
    )
}
