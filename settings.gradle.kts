pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        id("org.jetbrains.kotlin.plugin.noarg").version(extra["kotlin.version"] as String)
        id("org.jetbrains.kotlin.plugin.jpa").version(extra["kotlin.version"] as String)

    }
}

rootProject.name = "ComposeHibernate"

