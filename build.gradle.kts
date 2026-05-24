plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("io.freefair.lombok") version "8.10.2"
}

group = "chocola"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainClass.set("chocola.civilizationfiveeditor.Launcher")
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.dom4j:dom4j:2.1.4")
    implementation("org.controlsfx:controlsfx:11.2.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
