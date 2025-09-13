package com.anod.pecometer

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SpeedometerViewModelTest {
    
    @Test
    fun testInitialState() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = SpeedometerViewModel(testDispatcher)
        val initialState = viewModel.uiState.first()
        
        assertEquals(0f, initialState.currentSpeed)
        assertTrue(initialState.isMetric)
        assertFalse(initialState.isMoving)
        
        viewModel.cleanup()
    }
    
    @Test
    fun testUnitToggle() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = SpeedometerViewModel(testDispatcher)
        
        // Set a speed value first (simulate some speed)
        // Wait briefly for simulation to start
        kotlinx.coroutines.delay(100)
        
        val stateBeforeToggle = viewModel.uiState.first()
        viewModel.toggleSpeedUnit()
        val stateAfterToggle = viewModel.uiState.first()
        
        // Unit system should change
        assertFalse(stateAfterToggle.isMetric)
        
        // Speed should be converted (approximately)
        if (stateBeforeToggle.currentSpeed > 0) {
            val expectedSpeedInMph = stateBeforeToggle.currentSpeed * 0.621371f
            assertEquals(expectedSpeedInMph, stateAfterToggle.currentSpeed, 0.1f)
        }
        
        viewModel.cleanup()
    }
    
    @Test
    fun testSpeedSimulationEventuallyStarts() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = SpeedometerViewModel(testDispatcher)
        
        // Wait for simulation to kick in
        kotlinx.coroutines.delay(3000)
        testScheduler.advanceTimeBy(3000)
        
        val state = viewModel.uiState.first()
        
        // After simulation starts, speed should be greater than 0
        assertTrue(state.currentSpeed > 0f, "Expected speed to be greater than 0 after simulation starts")
        assertTrue(state.isMoving, "Expected isMoving to be true when speed > 0")
        
        viewModel.cleanup()
    }
}