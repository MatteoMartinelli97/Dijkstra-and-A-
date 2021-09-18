import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    application
}

group = "me.matteo"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("http://mvn.topobyte.de")
    }
    maven {
        url = uri("http://mvn.slimjars.com")
    }

}


dependencies {
    implementation ("de.topobyte:osm4j-core:0.1.0")
    implementation ("de.topobyte:osm4j-utils:0.1.3")
    implementation ("de.topobyte:osm4j-xml:0.1.3")
    implementation ("de.topobyte:osm4j-pbf:0.1.1")
    implementation ("de.topobyte:osm4j-tbo:0.1.0")
    implementation ("de.topobyte:osm4j-geometry:0.1.0")
    implementation("com.github.ajalt.clikt:clikt:3.1.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}