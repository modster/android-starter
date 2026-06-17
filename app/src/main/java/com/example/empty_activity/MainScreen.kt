package com.example.empty_activity

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.empty_activity.data.DataRepositoryImpl


@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier.Companion,
    viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(DataRepositoryImpl()) },
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  when (state) {
    MainScreenUiState.Loading    -> {
      Text("Loading")
    }
    is MainScreenUiState.Success -> {
      MainScreen(data = (state as MainScreenUiState.Success).data, modifier = modifier)
    }
    is MainScreenUiState.Error   -> {
        Text("Error loading data: ${(state as MainScreenUiState.Error).throwable.message}")
    }
  }
}
