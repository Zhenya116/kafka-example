package app;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {
    public static void main(String[] args) {
        var props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "my-group");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("auto.offset.reset", "earliest");

        var consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("test-topic"));

        while (true) {
            var records = consumer.poll(Duration.ofMillis(500));
            for (var rec : records) {
                System.out.printf(
                        "Got message: value=%s partition=%d offset=%d%n",
                        rec.value(), rec.partition(), rec.offset()
                );
            }
        }
    }
}
