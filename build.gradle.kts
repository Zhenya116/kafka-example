plugins {
    id("java")
}

group = "ru.webcontrol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka-clients:3.5.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.apache.kafka:kafka-clients:3.5.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}

