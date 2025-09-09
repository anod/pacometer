# Pecometer

A Kotlin Multiplatform speedometer application with Compose UI for tracking current driving speed and calculating pace projections.

## Features

- **Real-time Speed Display**: Shows current speed in km/h or mph
- **Unit Conversion**: Toggle between metric (km/h) and imperial (mph) units
- **Pace Calculations**: Shows time to complete fixed distances (10km, 1km, 20m or equivalent in miles)
- **Performance Projections**: Calculates time savings if speed increases by 10%
- **Cross-platform**: Built with Kotlin Multiplatform and Compose UI

## Demo Mode

Currently running in demo mode with simulated speed changes. The app demonstrates:
- Speed ranging from 0-120 km/h with realistic fluctuations
- Live updates every 2 seconds
- Automatic pace calculations for different distances
- Unit conversion between metric and imperial systems

## Building and Running

### Prerequisites
- JDK 11 or higher
- Gradle (included via wrapper)

### Build the project
```bash
./gradlew build
```

### Run desktop application
```bash
./gradlew run
```

Note: Requires display/GUI environment for desktop application.

## Architecture

- **Kotlin Multiplatform**: Common logic shared across platforms
- **Compose UI**: Modern declarative UI framework
- **MVVM Pattern**: Clean separation of concerns with ViewModel
- **Coroutines**: Asynchronous programming for real-time updates

## Future Enhancements

- GPS integration for real speed detection
- Android app with location permissions
- Speed history and statistics
- Route tracking and analysis
- Customizable distance goals