package app;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class KafkaProducerExample {
    public static void main(String[] args) {
        var props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        var producer = new KafkaProducer<String, String>(props);

        var record = new ProducerRecord<String, String>("test-topic", "key1", "Hello Kafka!");

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                System.err.println("Error: " + exception.getMessage());
            } else {
                System.out.printf(
                        "Published topic=%s partition=%d offset=%d%n",
                        metadata.topic(), metadata.partition(), metadata.offset()
                );
            }
        });

        producer.close();
    }
}
