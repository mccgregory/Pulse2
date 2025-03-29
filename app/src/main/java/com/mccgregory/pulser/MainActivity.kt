package com.mccgregory.pulser

import android.os.Bundle
import android.os.Vibrator
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
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
import java.util.Locale
// Test Commit with this change
// Colors used:
// 0xFFF44336: Red (Stop button)
// 0xFF4CAF50: Green (Start button)
// 0xFFFFFF00: Yellow (Interval buttons)
// 0xFF2196F3: Blue (Duration buttons)
// 0xFF000000: Black (Text)
// Test change
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
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Status text at top center
//--------------------------TEXT START----------------------------------------
            // Interval text
            Text(
                text = "${String.format(Locale.US, "%.1f", pulseInterval)}s",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
//                color = Color(0xFF4CAF50), // Green
                color = if (isVibrating) Color(0xFF4CAF50) else Color(0xFFF44336), // Green or Red
                modifier = Modifier
                    .offset(x = 85.dp, y = 76.dp)
                    .padding(2.dp)
            )
            // Duration text
            Text(
                text = "${pulseDuration}ms",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
//                color = Color(0xFF4CAF50), // Green
                color = if (isVibrating) Color(0xFF4CAF50) else Color(0xFFF44336), // Green or Red
                modifier = Modifier
                    .offset(x = 79.dp, y = 150.dp)
                    .padding(2.dp)
            )
            // Status text (Running/Stopped)
            Text(
                text = "${if (isVibrating) "Running" else "Stopped"} ",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = if (isVibrating) Color(0xFF4CAF50) else Color(0xFFF44336), // Green or Red
                modifier = Modifier
                    .offset(x = 75.dp, y = 45.dp)
                    .padding(2.dp)
            )
//---------------------------TEXT END---------------------------------------

            // Yellow Interval "-" button (left of Stop/Start)
            Button(
                onClick = { if (pulseInterval > 1.0f) pulseInterval -= 0.1f },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFF00)), // Yellow
                modifier = Modifier
                    .offset(x = (2).dp, y = 70.dp) // Left of center
                    .padding(1.dp)
            ) {
                Text(
                    text = "-",
                    fontSize = 40.sp, // Your big size!
                    color = Color(0xFF000000) // Black
                )
            }

            // Stop/Start button (center)
            Button(
                onClick = { isVibrating = !isVibrating },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isVibrating) Color(0xFFF44336) else Color(0xFF4CAF50) // Red or Green
                ),
                modifier = Modifier
                    .offset(x = 75.dp, y = -5.dp) // Middle
                    .padding(1.dp)
            ) {
                Text(
                    text = if (isVibrating) "Stop" else "Start",
                    fontSize = 20.sp,
                    color = Color(0xFF000000) // Black
                )
            }

            // Yellow Interval "+" button (right of Stop/Start)
            Button(
                onClick = { if (pulseInterval < 3.0f) pulseInterval += 0.1f },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFF00)), // Yellow
                modifier = Modifier
                    .offset(x = 158.dp, y = 70.dp) // Right of center
                    .padding(1.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 30.sp, // Your big size!
                    color = Color(0xFF000000) // Black
                )
            }

            // Blue Duration "-" button
            Button(
                onClick = { if (pulseDuration > 100) pulseDuration -= 50 },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)), // Blue
                modifier = Modifier
                    .offset(x = (17).dp, y = 130.dp) // Left, below top row
 //                   .padding(1.dp)
            ) {
                Text(
                    text = "-",
                    fontSize = 40.sp, // Your big size!
                    color = Color(0xFF000000) // Black
                )
            }

            // Blue Duration "+" button
            Button(
                onClick = { if (pulseDuration < 300) pulseDuration += 50 },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)), // Blue
                modifier = Modifier
                    .offset(x = 141.dp, y = 130.dp) // Right, below top row
                    .padding(1.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 30.sp, // Your big size!
                    color = Color(0xFF000000) // Black
                )
            }

            // Duration label at bottom
            Text(
//                text = "Duration (x=0, y=110)",
                text = "Duration",
                fontSize = 20.sp,
//                color = Color(0xFF000000), // Black
                color = Color(0xFF2196F3), // Blue
                modifier = Modifier
                    .offset(x = 66.dp, y = 163.dp)
                    .padding(2.dp)
            )
            // Duration label at bottom
            Text(
                text = "Interval",
                fontSize = 20.sp,
                color = Color(0xFFFFFF00), // Yellow
                modifier = Modifier
                    .offset(x = 71.dp, y = 92.dp)
                    .padding(2.dp)
            )
//=======================================
        }           // Box END
//=======================================
    }               // Scaffold END
}