package edu.kafka;

import edu.kafka.model.Message;
import io.vavr.collection.Stream;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SimpleConsumer {

    private static final long PERIOD_MS = 100;
    private static final Duration POOLING_TIMEOUT = Duration.ofMillis(PERIOD_MS);

    private final KafkaConsumer<String, Message> consumer;
    private final ScheduledExecutorService executorService;

    public static SimpleConsumer create(String groupId, String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9093");
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "edu.kafka.model.ValueMessageDeserializer");
        return new SimpleConsumer(topic, props);
    }

    private SimpleConsumer(String topic, Properties properties) {
        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(List.of(topic));
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void startPooling(Consumer<Message> messageConsumer) {
        executorService.scheduleAtFixedRate(() -> pool(messageConsumer), 0, PERIOD_MS, TimeUnit.MILLISECONDS);
    }

    private void pool(Consumer<Message> messageConsumer) {
        Stream.ofAll(consumer.poll(POOLING_TIMEOUT))
                .map(ConsumerRecord::value)
                .forEach(messageConsumer);
    }
}
