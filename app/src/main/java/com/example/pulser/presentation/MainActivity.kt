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

// Colors used:
// 0xFFF44336: Red (Stop button)
// 0xFF4CAF50: Green (Start button)
// 0xFFFFFF00: Yellow (Interval buttons)
// 0xFF2196F3: Blue (Duration buttons)
// 0xFF000000: Black (Text)

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
                .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "${String.format("%.1f", pulseInterval)}s\n${pulseDuration}ms\n${if (isVibrating) "Running" else "Stopped"}",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = if (isVibrating) Color(0xFF4CAF50) else Color(0xFFF44336), // Green or Red
                modifier = Modifier.padding(1.dp)
            )
            Row( // Top row: Yellow - | Stop/Start | Yellow +
                modifier = Modifier.padding(1.dp)
            ) {
                Button(
                    onClick = { if (pulseInterval > 1.0f) pulseInterval -= 0.1f },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFF00)), // Yellow
                    modifier = Modifier.padding(1.dp)
                ) {
                    Text(
                        text = "-",
                        fontSize = 12.sp,
                        color = Color(0xFF000000) // Black
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Button(
                    onClick = { isVibrating = !isVibrating },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isVibrating) Color(0xFFF44336) else Color(0xFF4CAF50) // Red or Green
                    ),
                    modifier = Modifier.padding(1.dp)
                ) {
                    Text(
                        text = if (isVibrating) "Stop" else "Start",
                        fontSize = 12.sp,
                        color = Color(0xFF000000) // Black
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Button(
                    onClick = { if (pulseInterval < 3.0f) pulseInterval += 0.1f },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFF00)), // Yellow
                    modifier = Modifier.padding(1.dp)
                ) {
                    Text(
                        text = "+",
                        fontSize = 12.sp,
                        color = Color(0xFF000000) // Black
                    )
                }
            }
            Row( // Middle row: Blue - | Blue +
                modifier = Modifier.padding(1.dp)
            ) {
                Button(
                    onClick = { if (pulseDuration > 100) pulseDuration -= 50 },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)), // Blue
                    modifier = Modifier.padding(1.dp)
                ) {
                    Text(
                        text = "-",
                        fontSize = 12.sp,
                        color = Color(0xFF000000) // Black
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Button(
                    onClick = { if (pulseDuration < 300) pulseDuration += 50 },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)), // Blue
                    modifier = Modifier.padding(1.dp)
                ) {
                    Text(
                        text = "+",
                        fontSize = 12.sp,
                        color = Color(0xFF000000) // Black
                    )
                }
            }
            Row( // Bottom row: Duration label
                modifier = Modifier.padding(1.dp)
            ) {
                Text(
                    text = "Duration",
                    fontSize = 10.sp,
                    color = Color(0xFF000000) // Black
                )
            }
        }
    }
}