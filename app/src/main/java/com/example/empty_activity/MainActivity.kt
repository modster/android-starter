package com.example.empty_activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.empty_activity.gesture.ChopDetector
import com.example.empty_activity.theme.EmptyActivityTheme
import com.example.empty_activity.torch.TorchController
import com.example.empty_activity.ui.flashlight.FlashlightViewModel

class MainActivity : ComponentActivity()
{
    private val torchController by lazy { TorchController(applicationContext) }
    private val flashlightViewModel: FlashlightViewModel by viewModels {
        object : ViewModelProvider.Factory
        {
            override fun <T : ViewModel> create(modelClass: Class<T>): T
            {
                @Suppress("UNCHECKED_CAST")
                return FlashlightViewModel(torchController) as T
            }
        }
    }
    private lateinit var chopDetector: ChopDetector


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        chopDetector = ChopDetector(applicationContext) {
            if (flashlightViewModel.uiState.value.hasCameraPermission) {
                flashlightViewModel.toggleTorch()
            }
        }
        setContent {
            EmptyActivityTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                ) { MainNavigation(flashlightViewModel = flashlightViewModel) }
            }
        }
    }

    override fun onStart()
    {
        super.onStart()
        chopDetector.start()
    }

    override fun onStop()
    {
        chopDetector.stop()
        super.onStop()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        if (flashlightViewModel.onVolumeKey(keyCode)) {
            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}
