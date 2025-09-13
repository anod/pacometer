#!/bin/bash

# Pecometer APK Build Setup Script
# This script switches the build configuration to enable Android APK building

set -e

echo "🚀 Setting up Pecometer for Android APK building..."

# Check if we're in the right directory
if [ ! -f "build.gradle.kts" ]; then
    echo "❌ Error: Please run this script from the project root directory"
    exit 1
fi

# Backup current build file
echo "📋 Backing up current build configuration..."
cp build.gradle.kts build-desktop.gradle.kts

# Switch to Android build configuration
echo "🔄 Switching to Android build configuration..."
cp build-android.gradle.kts build.gradle.kts

# Check if Android SDK is available
if [ -z "$ANDROID_HOME" ] && [ ! -d "/usr/lib/android-sdk" ]; then
    echo "⚠️  Warning: ANDROID_HOME not set and default Android SDK not found"
    echo "💡 Please ensure Android SDK is installed and ANDROID_HOME is set"
    echo "   or use GitHub Actions to build the APK automatically"
fi

# Create local.properties if it doesn't exist
if [ ! -f "local.properties" ]; then
    echo "📝 Creating local.properties..."
    if [ -n "$ANDROID_HOME" ]; then
        echo "sdk.dir=$ANDROID_HOME" > local.properties
    elif [ -d "/usr/lib/android-sdk" ]; then
        echo "sdk.dir=/usr/lib/android-sdk" > local.properties
    else
        echo "# sdk.dir=/path/to/your/android-sdk" > local.properties
        echo "⚠️  Please edit local.properties and set the correct Android SDK path"
    fi
fi

echo "✅ Setup complete!"
echo ""
echo "📱 To build APK files:"
echo "   Debug APK:    ./gradlew assembleDebug"
echo "   Release APK:  ./gradlew assembleRelease"
echo ""
echo "📁 APK files will be created in:"
echo "   build/outputs/apk/debug/"
echo "   build/outputs/apk/release/"
echo ""
echo "🔄 To revert to desktop-only build:"
echo "   cp build-desktop.gradle.kts build.gradle.kts"
echo ""
echo "🌐 Alternatively, use GitHub Actions for automatic APK building"
echo "   (see .github/workflows/build-apk.yml)"