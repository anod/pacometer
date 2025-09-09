plugins {
    kotlin("multiplatform") version "1.9.21"
    id("org.jetbrains.compose") version "1.5.11"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.anod.pecometer.MainKt"
    }
}

// Add a task to run the demo
tasks.register<JavaExec>("demo") {
    group = "application"
    description = "Run Pecometer speed calculation demo"
    dependsOn("compileKotlinJvm")
    classpath = configurations["jvmRuntimeClasspath"] + kotlin.jvm().compilations["main"].output.allOutputs
    mainClass.set("com.anod.pecometer.DemoKt")
}