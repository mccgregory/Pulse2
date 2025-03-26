package com.example.pulser.presentation

import android.os.Bundle
import android.os.Vibrator
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import android.os.VibrationEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pulse2App(getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        }
    }
}

@Composable
fun Pulse2App(vibrator: Vibrator) {
    var pulseInterval by remember { mutableStateOf(1.0f) } // Pulse cycle (1-3s)
    var pulseDuration by remember { mutableStateOf(200L) } // Vibration length (100-300ms)
    var isVibrating by remember { mutableStateOf(true) }

    LaunchedEffect(pulseInterval, pulseDuration, isVibrating) {
        if (isVibrating) {
            val sleepMs = (pulseInterval * 1000).toLong() - pulseDuration
            val pattern = longArrayOf(0, pulseDuration, sleepMs)
            val effect = VibrationEffect.createWaveform(pattern, 0)
            vibrator.cancel()
            vibrator.vibrate(effect)
        } else {
            vibrator.cancel()
        }
    }

    Scaffold(
        timeText = { TimeText() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${String.format("%.1f", pulseInterval)}s\n${pulseDuration}ms\n${if (isVibrating) "Running" else "Stopped"}",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { isVibrating = !isVibrating }
            ) {
                Text(if (isVibrating) "Stop" else "Start")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = { if (pulseInterval > 1.0f) pulseInterval -= 0.1f }
                ) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { if (pulseInterval < 3.0f) pulseInterval += 0.1f }
                ) {
                    Text("+")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = { if (pulseDuration > 100) pulseDuration -= 50 }
                ) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { if (pulseDuration < 300) pulseDuration += 50 }
                ) {
                    Text("+")
                }
            }
        }
    }
}