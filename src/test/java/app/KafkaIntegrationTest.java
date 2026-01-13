package app;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TopicExistsException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaIntegrationTest {

    @Test
    void shouldPublishAndConsumeMessage() throws Exception {
        String topic = "test-topic-" + System.currentTimeMillis();

        // create topic
        var adminProps = new Properties();
        adminProps.put("bootstrap.servers", "localhost:9092");
        try (var admin = AdminClient.create(adminProps)) {
            try {
                admin.createTopics(List.of(new NewTopic(topic, 1, (short) 1)))
                        .all().get();
            } catch (ExecutionException ex) {
                if (!(ex.getCause() instanceof TopicExistsException)) {
                    throw ex;
                }
            }
        }

        // produce message
        var prodProps = new Properties();
        prodProps.put("bootstrap.servers", "localhost:9092");
        prodProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prodProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (var producer = new KafkaProducer<String, String>(prodProps)) {
            producer.send(new ProducerRecord<>(topic, "hello", "world")).get();
        }

        // consume message
        var consProps = new Properties();
        consProps.put("bootstrap.servers", "localhost:9092");
        consProps.put("group.id", "test-group-" + System.currentTimeMillis());
        consProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consProps.put("auto.offset.reset", "earliest");
        consProps.put("enable.auto.commit", "false");

        try (var consumer = new KafkaConsumer<String, String>(consProps)) {
            consumer.subscribe(Collections.singletonList(topic));

            consumer.poll(Duration.ofMillis(0));

            ConsumerRecords<String, String> records = ConsumerRecords.empty();
            long start = System.currentTimeMillis();
            while (records.isEmpty() && System.currentTimeMillis() - start < 5000) {
                records = consumer.poll(Duration.ofMillis(500));
            }

            assertEquals(1, records.count());
        }
    }
}
