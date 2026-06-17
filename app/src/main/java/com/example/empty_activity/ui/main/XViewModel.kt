package com.example.empty_activity.ui.main

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import java.io.File

class XViewModel : ViewModel() {
    fun capturePhoto(context: Context, imageCapture: ImageCapture) {
        val outputFile = File(context.cacheDir, "media3_effect_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(outputFile)
            .build()

        val executor = ContextCompat.getMainExecutor(context)
        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("CameraMedia3", "Filtered photo saved successfully: ${outputFile.absolutePath}")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraMedia3", "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }
}
