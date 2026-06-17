package com.example.empty_activity

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.empty_activity.data.DataRepositoryImpl
import com.example.empty_activity.di.appModule
import com.greeffer.xcam.ui.theme.XTheme
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey


@Composable
internal fun MainScreen(data: List<String>, modifier: Modifier = Modifier.Companion) {
    Column(modifier) { data.forEach { Greeting(it) } }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier.Companion) {
    Text(text = "Hello $name!", modifier = modifier)
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    XTheme { MainScreen(listOf("Android")) }
}


@Preview(showBackground = true, widthDp = 340)
@Composable
fun MainScreenPortraitPreview() {
    XTheme { MainScreen(listOf("Android")) }
}
