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
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import android.os.VibrationEffect
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        setContent {
            Pulse2App(vibrator)
        }
    }
}

@Composable
fun Pulse2App(vibrator: Vibrator) {
    var pulseInterval by remember { mutableStateOf(1.0f) }
    var pulseDuration by remember { mutableStateOf(200L) }
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

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${String.format("%.1f", pulseInterval)}s\n${pulseDuration}ms\n${if (isVibrating) "Running" else "Stopped"}",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = if (isVibrating) Color(0xFF4CAF50) else Color(0xFFF44336),
                modifier = Modifier.padding(4.dp)
            )

            Button(
                onClick = { isVibrating = !isVibrating },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isVibrating) Color(0xFFF44336) else Color(0xFF4CAF50)
                ),
                modifier = Modifier.padding(4.dp)
            ) {
                Text(if (isVibrating) "Stop" else "Start")
            }

            Row(
                modifier = Modifier.padding(4.dp)
            ) {
                Button(
                    onClick = { if (pulseInterval > 1.0f) pulseInterval -= 0.1f },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF5722)),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { if (pulseInterval < 3.0f) pulseInterval += 0.1f },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF5722)),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text("+")
                }
            }

            Row(
                modifier = Modifier
                    //.background(Color(0xFF2196F3)) // Removed blue background temporarily
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Duration", fontSize = 14.sp, color = Color.Black) // Black for visibility
            }
            Row(
                modifier = Modifier.padding(4.dp)
            ) {
                Button(
                    onClick = { if (pulseDuration > 100) pulseDuration -= 50 },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { if (pulseDuration < 300) pulseDuration += 50 },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text("+")
                }
            }
        }
    }
}