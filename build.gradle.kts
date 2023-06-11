import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "pony.rating"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}



kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }



    sourceSets {

        val jvmMain by getting {

            dependencies {
                implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
                implementation(compose.desktop.currentOs)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)

                implementation("org.chenliang.oggus:oggus:1.2.0")
                implementation("com.google.code.gson:gson:2.7")
                implementation("com.dorkbox:Notify:3.7")
                implementation("com.lordcodes.turtle:turtle:0.8.0")
            }
        }
        val jvmTest by getting
    }

}
tasks {
    withType<org.gradle.jvm.tasks.Jar> {
        exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    }
}

compose.desktop {

    application {
        mainClass = "MainKt"
        nativeDistributions {
            modules("java.compiler", "java.instrument", "java.sql", "jdk.unsupported")
            targetFormats(TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "PlaylistRating"
            packageVersion = "1.0.0"
        }
    }
}

