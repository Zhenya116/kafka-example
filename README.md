# Kafka Integration Test Example (Java + JUnit5)

Пример простого интеграционного теста Kafka на Java с использованием:

- Kafka AdminClient
- Kafka Producer / Consumer API
- JUnit 5
- Чистый код без зависимостей от фреймворков

Тест гарантированно отправляет и потребляет **одно сообщение** в уникальный топик.

---

## Зависимости (Gradle)

```gradle
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

---

## Как работает тест

1. Создаёт уникальный Kafka топик (test-topic-<timestamp>)

2. Отправляет одно сообщение в топик

3. Подписывается на него с новым group.id

4. Ожидает сообщения до 5 секунд

5. Проверяет, что пришло ровно одно сообщение


