# Installation

Requirements

- PaperMC API: the project declares compileOnly dependency on `io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT` (see build.gradle.kts). Use a Paper server matching 1.20.4.
- Kotlin: plugin uses Kotlin JVM plugin `2.2.20` and targets Java 17 (toolchain configured in build.gradle.kts).
- Java: JDK 17 for compilation/runtime.

Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("io.github.trainingbear:jaksara-inventory:1.0.0") // replace with published coordinates
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}
```

Gradle (Groovy)

```groovy
dependencies {
    implementation 'io.github.trainingbear:jaksara-inventory:1.0.0'
    compileOnly 'io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT'
}
```

Maven

```xml
<dependency>
  <groupId>io.github.trainingbear</groupId>
  <artifactId>jaksara-inventory</artifactId>
  <version>1.0.0</version>
</dependency>
```

Initialization

Call CustomMenu.init(plugin) in your plugin `onEnable()` to register internal listeners (inventory / chat input).

```kotlin
class MyPlugin: JavaPlugin() {
  override fun onEnable() {
    CustomMenu.init(this)
  }
}
```


Compatibility

The library compiles against Paper 1.20.4 API and Kotlin 2.2.20 plugin. It targets Java 17.
