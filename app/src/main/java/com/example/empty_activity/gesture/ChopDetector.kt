package com.example.empty_activity.gesture

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs

class ChopDetector(
  context: Context,
  private val onDoubleChop: () -> Unit,
) : SensorEventListener {
  private val sensorManager = context.getSystemService(SensorManager::class.java)
  private val linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

  private var firstChopTimestampNanos = 0L
  private var lastToggleTimestampNanos = 0L
  private var listening = false

  fun start() {
    if (!listening && linearAccelerationSensor != null) {
      sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_GAME)
      listening = true
    }
  }

  fun stop() {
    if (listening) {
      sensorManager.unregisterListener(this)
      listening = false
      firstChopTimestampNanos = 0L
    }
  }

  override fun onSensorChanged(event: SensorEvent) {
    if (event.sensor.type != Sensor.TYPE_LINEAR_ACCELERATION) return

    val timestamp = event.timestamp
    val zAcceleration = abs(event.values[2])
    if (zAcceleration < CHOP_ACCELERATION_THRESHOLD) return

    if (timestamp - lastToggleTimestampNanos < TOGGLE_COOLDOWN_NANOS) return

    val firstChop = firstChopTimestampNanos
    if (firstChop == 0L || timestamp - firstChop > DOUBLE_CHOP_WINDOW_NANOS) {
      firstChopTimestampNanos = timestamp
      return
    }

    firstChopTimestampNanos = 0L
    lastToggleTimestampNanos = timestamp
    onDoubleChop()
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

  private companion object {
    const val CHOP_ACCELERATION_THRESHOLD = 15f
    const val DOUBLE_CHOP_WINDOW_NANOS = 500_000_000L
    const val TOGGLE_COOLDOWN_NANOS = 1_000_000_000L
  }
}
