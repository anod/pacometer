package com.anod.pecometer

fun main() {
    println("=== Pecometer Speed Calculator Demo ===")
    println()
    
    // Demo different speeds
    val testSpeeds = listOf(30f, 50f, 80f, 120f)
    
    for (speed in testSpeeds) {
        println("Current Speed: ${speed} km/h")
        println("  Metric distances:")
        
        val distances = listOf(
            "10 km" to 10.0,
            "1 km" to 1.0,
            "20 m" to 0.02
        )
        
        distances.forEach { (label, distance) ->
            val currentTime = calculateTime(distance, speed.toDouble())
            val fasterTime = calculateTime(distance, speed.toDouble() * 1.1)
            val timeSaved = calculateTimeSaved(distance, speed.toDouble())
            
            println("    $label: $currentTime (10% faster: $fasterTime, save: $timeSaved)")
        }
        
        // Show speed in mph
        val speedMph = speed * 0.621371f
        println("  In Imperial: ${String.format("%.1f", speedMph)} mph")
        
        val distancesImperial = listOf(
            "10 miles" to 10.0,
            "1 mile" to 1.0,
            "20 yards" to 0.011364
        )
        
        println("  Imperial distances:")
        distancesImperial.forEach { (label, distance) ->
            val currentTime = calculateTime(distance, speedMph.toDouble())
            val fasterTime = calculateTime(distance, speedMph.toDouble() * 1.1)
            val timeSaved = calculateTimeSaved(distance, speedMph.toDouble())
            
            println("    $label: $currentTime (10% faster: $fasterTime, save: $timeSaved)")
        }
        
        println()
    }
    
    // Demo unit conversion
    println("=== Unit Conversion Demo ===")
    println("50 km/h = ${String.format("%.1f", 50f * 0.621371f)} mph")
    println("30 mph = ${String.format("%.1f", 30f / 0.621371f)} km/h")
    println()
    
    println("Demo completed! 🚗💨")
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