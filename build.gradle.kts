plugins {
    kotlin("jvm") version "2.2.20"
    `java-library`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("com.gradleup.shadow") version "9.0.0"
}

group = "io.github.trainingbear"
version = "1.2.2-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://mvn.wesjd.net/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
}
dependencies {
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")
    implementation(kotlin("reflect"))
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.4")
    implementation("net.wesjd:anvilgui:1.10.13-SNAPSHOT")
    api("net.kyori:adventure-api:4.14.0")
    api("net.kyori:adventure-text-minimessage:4.14.0")
}
paperweight {
    reobfArtifactConfiguration =
        io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION
}
tasks.shadowJar {
    archiveClassifier.set("")
     relocate(
        "net.wesjd.anvilgui",
        "io.github.trainingbear.jaksara.inventory.internal.anvilgui"
    )
    minimize {
        exclude(dependency("net.wesjd:anvilgui:.*"))
    }
}
tasks.assemble {
    dependsOn(tasks.reobfJar)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            from(components["shadow"])

            groupId = project.group.toString()
            artifactId = "jaksara-inventory"
            version = project.version.toString()
        }
    }
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
