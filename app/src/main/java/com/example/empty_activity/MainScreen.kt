package com.example.empty_activity

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.empty_activity.data.DataRepositoryImpl
import com.example.empty_activity.ui.main.CameraWithMedia3EffectScreen
import com.example.empty_activity.ui.main.XViewModel


@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier.Companion,
    viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(DataRepositoryImpl()) },
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val xViewModel: XViewModel = viewModel()
  when (state) {
    MainScreenUiState.Loading    -> {
      Text("Loading")
    }
     is MainScreenUiState.Success -> {
         CameraWithMedia3EffectScreen(vm = xViewModel)
     }
    is MainScreenUiState.Error   -> {
        Text("Error loading data: ${(state as MainScreenUiState.Error).throwable.message}")
    }
  }
}
