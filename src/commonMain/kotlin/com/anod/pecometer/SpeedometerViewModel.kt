package com.anod.pecometer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.*

data class SpeedometerUiState(
    val currentSpeed: Float = 0f,
    val isMetric: Boolean = true,
    val isMoving: Boolean = false
)

class SpeedometerViewModel {
    private val _uiState = MutableStateFlow(SpeedometerUiState())
    val uiState: StateFlow<SpeedometerUiState> = _uiState.asStateFlow()
    
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    init {
        // Start simulating speed changes for demo
        startSpeedSimulation()
    }
    
    fun toggleSpeedUnit() {
        val currentState = _uiState.value
        val newSpeed = if (currentState.isMetric) {
            // Convert km/h to mph
            currentState.currentSpeed * 0.621371f
        } else {
            // Convert mph to km/h
            currentState.currentSpeed / 0.621371f
        }
        
        _uiState.value = currentState.copy(
            currentSpeed = newSpeed,
            isMetric = !currentState.isMetric
        )
    }
    
    private fun startSpeedSimulation() {
        scope.launch {
            var speed = 0f
            while (true) {
                delay(2000) // Update every 2 seconds
                
                // Simulate realistic speed changes
                speed = when {
                    speed == 0f -> 25f
                    speed < 80f -> speed + (-5f + kotlin.random.Random.nextFloat() * 15f)
                    else -> speed + (-10f + kotlin.random.Random.nextFloat() * 15f)
                }.coerceIn(0f, 120f)
                
                val currentState = _uiState.value
                _uiState.value = currentState.copy(
                    currentSpeed = if (currentState.isMetric) speed else speed * 0.621371f,
                    isMoving = speed > 1f
                )
            }
        }
    }
    
    fun cleanup() {
        scope.cancel()
    }
}