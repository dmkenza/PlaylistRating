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
                implementation(compose.materialIconsExtended)
                implementation("commons-io:commons-io:2.13.0")

                implementation("org.chenliang.oggus:oggus:1.2.0")
                implementation("com.google.code.gson:gson:2.7")
                implementation("com.dorkbox:Notify:3.7")

                implementation ("net.dv8tion:JDA:5.0.0-alpha.12")
                implementation ("org.jetbrains.exposed:exposed-core:0.38.2")
                implementation ("org.jetbrains.exposed:exposed-jdbc:0.38.2")
                implementation ("org.xerial:sqlite-jdbc:3.36.0.3")

                implementation ("com.github.Adonai:jaudiotagger:2.3.14")
//                implementation("net.jthink:jaudiotagger:3.0.1")
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

