plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.zibble"
version = "2.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.mattmalec.com/repository/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.10")
    implementation("club.minnced:discord-webhooks:0.8.2")
    implementation("com.mattmalec:Pterodactyl4J:2.BETA_93")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("io.lettuce:lettuce-core:6.2.0.RELEASE")
    implementation("com.google.guava:guava:31.1-jre")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    
    shadowJar {
        archiveFileName.set("ConsoleLogger.jar")
        minimize()
    }

    jar {
        manifest {
            attributes["Main-Class"] = "org.zibble.consolelogger.ConsoleLoggerKt"
        }
    }

    compileKotlin {
        kotlinOptions.suppressWarnings = true
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

sourceSets {
    main {
        java {
            srcDir("src/main/kotlin")
        }
        resources {
            srcDir("src/main/resources")
        }
    }
}