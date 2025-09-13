# Pecometer Android APK Building Guide

This document explains how to build Android APK files for the Pecometer speedometer app.

## Current Status

The project is now configured to support both JVM (desktop) and Android platforms. However, due to network restrictions in the current environment, the Android Gradle Plugin cannot be downloaded directly.

## Option 1: GitHub Actions (Automated APK Building)

The easiest way to build APK files is using GitHub Actions, which has access to all required dependencies:

1. The workflow file `.github/workflows/build-apk.yml` is already configured
2. Push your changes to the repository 
3. The workflow will automatically build both debug and release APKs
4. Download the APK artifacts from the Actions tab

### Workflow Features:
- Builds both debug and release APKs
- Caches dependencies for faster builds
- Uploads APK files as downloadable artifacts
- Retains debug APKs for 7 days, release APKs for 30 days

## Option 2: Local Development Environment

If you have a local development environment with Android SDK:

### Prerequisites:
1. Android Studio or Android SDK Command Line Tools
2. Set ANDROID_HOME environment variable
3. Internet access to download Android Gradle Plugin

### Setup Steps:

1. **Replace build configuration**:
   ```bash
   cp build-android.gradle.kts build.gradle.kts
   ```

2. **Build APK**:
   ```bash
   ./gradlew assembleDebug      # For debug APK
   ./gradlew assembleRelease    # For release APK  
   ```

3. **Find your APK**:
   - Debug: `build/outputs/apk/debug/pecometer-debug.apk`
   - Release: `build/outputs/apk/release/pecometer-release.apk`

### Android Configuration Details:
- **Package Name**: `com.anod.pecometer`
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Features**: Jetpack Compose UI, Material 3 design

## Option 3: Alternative Build Methods

### Using Android Studio:
1. Open the project in Android Studio
2. Replace `build.gradle.kts` with `build-android.gradle.kts`
3. Sync project
4. Build → Build Bundle(s) / APK(s) → Build APK(s)

### Using Command Line (if Android tools are available):
```bash
# Setup Android SDK path
echo "sdk.dir=/path/to/your/android-sdk" > local.properties

# Build APK
./gradlew assembleRelease
```

## Application Features

The Android APK will include:
- **Speedometer Display**: Real-time speed showing with unit conversion (km/h ↔ mph)
- **Time Calculations**: Shows estimated time for different distances at current speed
- **Pace Analysis**: Displays time savings when traveling 10% faster  
- **Material 3 UI**: Modern Android design with dynamic theming support
- **Demo Mode**: Simulated speed data for testing

## Installation

To install the APK on an Android device:
1. Enable "Install unknown apps" for your file manager or browser
2. Download the APK file to your device
3. Open the APK file and follow installation prompts
4. Grant any requested permissions

## File Structure

The Android-specific files created:
```
src/androidMain/
├── AndroidManifest.xml          # App configuration and permissions
└── kotlin/com/anod/pecometer/
    └── MainActivity.kt          # Android entry point

.github/workflows/
└── build-apk.yml               # Automated APK build workflow

build-android.gradle.kts        # Android-enabled build configuration
```

## Troubleshooting

**Build fails with "Plugin not found"**: This indicates the Android Gradle Plugin cannot be downloaded. Use GitHub Actions or a local environment with internet access.

**APK won't install**: Ensure "Install unknown apps" is enabled and the APK is not corrupted during download.

**App crashes on startup**: Check that the target device meets the minimum SDK requirement (Android 7.0+).