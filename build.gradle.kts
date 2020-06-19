import ratpack.gradle.RatpackPlugin

buildscript {
    dependencies {
        classpath("io.ratpack:ratpack-gradle:1.8.0")
    }
}

plugins {
    kotlin("jvm") version "1.3.72"
    groovy
    application
}

apply {
    plugin<RatpackPlugin>()
}

application {
    mainClassName = "me.kcybulski.chinachat.AppKt"
}

group = "me.kcybulski"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.reactivex.rxjava2", "rxjava", "2.2.19")
    implementation("com.auth0", "java-jwt", "3.10.3")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.11.0")
    implementation("org.slf4j", "slf4j-simple", "2.0.0-alpha1")
    implementation("log4j", "log4j", "1.2.17")

    testImplementation("io.ratpack", "ratpack-test", "1.8.0")
    testImplementation("org.codehaus.groovy", "groovy-all", "2.5.11")
    testImplementation("org.spockframework", "spock-core", "2.0-M2-groovy-3.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    "test"(Test::class) {
        useJUnitPlatform()
    }
}
