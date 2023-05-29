import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.kotlin.plugin.jpa")

}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                // https://mvnrepository.com/artifact/com.h2database/h2
                implementation("com.h2database:h2:2.1.214")
                // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
                implementation("org.hibernate.orm:hibernate-core:6.2.3.Final")
                // https://mvnrepository.com/artifact/com.google.code.gson/gson
                implementation("com.google.code.gson:gson:2.10.1")



            }
        }

        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "getDataFromRestApi"
            packageVersion = "1.0.0"
        }
    }
}
