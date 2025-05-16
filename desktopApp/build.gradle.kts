import org.jetbrains.compose.desktop.application.dsl.TargetFormat

// Hardcoded versions to avoid property reference issues
plugins {
    kotlin("jvm") version "2.0.0"
    id("org.jetbrains.compose") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" // Required for Kotlin 2.0 with Compose
    kotlin("plugin.serialization") version "2.0.0" // For kotlinx.serialization
}

// Hardcoded versions directly (avoid property reference issues)
val kotlinVersion = "2.0.0"
val composeVersion = "1.6.10"
val langchain4jVersion = "1.0.0-rc1"
val kotlinxCoroutinesVersion = "1.8.0"
val kotlinxSerializationVersion = "1.6.3"
val koinVersion = "4.0.4"
val ktorVersion = "2.3.9"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Compose Desktop
    implementation(compose.desktop.currentOs)

    // KotlinX Coroutines (often used with Compose and for async tasks)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$kotlinxCoroutinesVersion") // For integration with Swing main loop

    // KotlinX Serialization for config file
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    // Langchain4j core
    implementation("dev.langchain4j:langchain4j:$langchain4jVersion")

    // Gemini model integration
    implementation("dev.langchain4j:langchain4j-google-ai-gemini:1.0.0-beta4")

    // Koin
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-compose:$koinVersion")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("ch.qos.logback:logback-classic:1.5.3")

    // Ktor HTTP Client
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
}

compose.desktop {
    application {
        mainClass = "com.example.huulkit.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Huulkit"
            packageVersion = "1.0.0"
            // Set application icon
            windows {
                iconFile.set(project.file("src/main/resources/icon.png"))
            }
        }
    }
}

kotlin {
    jvmToolchain(21) // Using Java 21
}

// Ensure build.gradle.kts in root can access properties
project.rootProject.extra.set("kotlin.version", kotlinVersion)
project.rootProject.extra.set("compose.version", composeVersion)
project.rootProject.extra.set("langchain4j.version", langchain4jVersion)
project.rootProject.extra.set("kotlinx.coroutines.version", kotlinxCoroutinesVersion)
project.rootProject.extra.set("kotlinx.serialization.version", kotlinxSerializationVersion) 
