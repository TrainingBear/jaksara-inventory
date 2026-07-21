plugins {
    kotlin("jvm") version "2.2.20"
    `java-library`
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "io.github.trainingbear"
version = "1.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.4")

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    api("net.kyori:adventure-api:4.14.0")
    api("net.kyori:adventure-text-minimessage:4.14.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}

mavenPublishing {
    publishToMavenCentral()

    coordinates(
        group.toString(),
        "jaksara-inventory",
        version.toString()
    )

    if (!version.toString().endsWith("-SNAPSHOT")) {
        signAllPublications()
    }

    pom {
        name.set("Jaksara Inventory")
        description.set("Inventory DSL framework for PaperMC")
        inceptionYear.set("2026")
        url.set("https://github.com/TrainingBear/jaksara-inventory")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("TrainingBear")
                name.set("Kukuh Sudrajad")
            }
        }

        scm {
            url.set("https://github.com/TrainingBear/jaksara-inventory")
            connection.set("scm:git:git://github.com/TrainingBear/jaksara-inventory.git")
            developerConnection.set("scm:git:ssh://git@github.com/TrainingBear/jaksara-inventory.git")
        }
    }
}