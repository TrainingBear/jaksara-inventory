plugins {
    `java-library`
    `maven-publish`
    signing
    kotlin("jvm") version "2.2.20"
    id("org.jreleaser") version "1.18.0"
}

group = "me.jaksara"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.4")
    api("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    api("net.kyori:adventure-api:4.14.0")
    api("net.kyori:adventure-text-minimessage:4.14.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withSourcesJar()
    withJavadocJar()
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "io.github.trainingbear"
            artifactId = "jaksara-inventory"
            version = "1.0.0"

            pom {
                name.set("Jaksara Inventory")
                description.set("Inventory / DSL framework for PaperMC")
                url.set("https://github.com/TrainingBear/jaksara-inventory")

                licenses {
                    license {
                        name.set("MIT License")
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
                    connection.set("scm:git:https://github.com/TrainingBear/jaksara-inventory.git")
                    developerConnection.set("scm:git:ssh://git@github.com/TrainingBear/jaksara-inventory.git")
                }
            }
        }
    }
}
signing {
    val signingKey: String? by project
    val signingPassword: String? by project

    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}