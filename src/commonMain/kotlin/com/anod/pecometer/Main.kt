package com.anod.pecometer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.*

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Pecometer") {
        App()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val viewModel = remember { SpeedometerViewModel() }
    val uiState by viewModel.uiState.collectAsState()
    
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Pecometer") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        ) { paddingValues ->
            SpeedometerScreen(
                uiState = uiState,
                onToggleUnit = viewModel::toggleSpeedUnit,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun SpeedometerScreen(
    uiState: SpeedometerUiState,
    onToggleUnit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Current Speed Display
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Current Speed",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                SelectionContainer {
                    Text(
                        text = String.format("%.1f", uiState.currentSpeed),
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Button(
                    onClick = onToggleUnit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(
                        text = if (uiState.isMetric) "km/h" else "mph",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        // Peace Speed Calculations
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Time to Complete",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                val distances = if (uiState.isMetric) {
                    listOf(
                        "10 km" to 10.0,
                        "1 km" to 1.0,
                        "20 m" to 0.02
                    )
                } else {
                    listOf(
                        "10 miles" to 10.0,
                        "1 mile" to 1.0,
                        "20 yards" to 0.011364 // 20 yards in miles
                    )
                }
                
                distances.forEach { (label, distance) ->
                    PaceCalculationRow(
                        distance = label,
                        currentTime = calculateTime(distance, uiState.currentSpeed.toDouble()),
                        fasterTime = calculateTime(distance, uiState.currentSpeed.toDouble() * 1.1),
                        timeSaved = calculateTimeSaved(distance, uiState.currentSpeed.toDouble())
                    )
                    if (distance != distances.last().second) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        
        // Speed Status
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.isMoving) 
                    MaterialTheme.colorScheme.tertiaryContainer 
                else 
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.isMoving) "🚗 Moving" else "🛑 Stationary",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (uiState.isMoving) 
                        MaterialTheme.colorScheme.onTertiaryContainer 
                    else 
                        MaterialTheme.colorScheme.onErrorContainer
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "Demo Mode",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun PaceCalculationRow(
    distance: String,
    currentTime: String,
    fasterTime: String,
    timeSaved: String
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = distance,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = currentTime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "10% faster: $fasterTime",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Save: $timeSaved",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4CAF50), // Green color
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun calculateTime(distanceKm: Double, speedKmh: Double): String {
    if (speedKmh <= 0) return "∞"
    
    val timeHours = distanceKm / speedKmh
    val timeMinutes = timeHours * 60
    val timeSeconds = timeMinutes * 60
    
    return when {
        timeHours >= 1 -> String.format("%.1fh", timeHours)
        timeMinutes >= 1 -> String.format("%.1fm", timeMinutes)
        else -> String.format("%.0fs", timeSeconds)
    }
}

private fun calculateTimeSaved(distanceKm: Double, currentSpeedKmh: Double): String {
    if (currentSpeedKmh <= 0) return "0s"
    
    val currentTime = distanceKm / currentSpeedKmh * 3600 // in seconds
    val fasterTime = distanceKm / (currentSpeedKmh * 1.1) * 3600 // 10% faster
    val timeSavedSeconds = currentTime - fasterTime
    
    return when {
        timeSavedSeconds >= 60 -> String.format("%.1fm", timeSavedSeconds / 60)
        else -> String.format("%.0fs", timeSavedSeconds)
    }
}