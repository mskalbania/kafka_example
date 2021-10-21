package edu.kafka;

import edu.kafka.model.Message;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SimpleProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleProducer.class);
    private final KafkaProducer<String, Message> producer;
    private final String topic;

    public static SimpleProducer create(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9093");
        props.put("acks", "all");
        props.put("compression.type", "gzip");
        props.put("partitioner.class", "edu.kafka.MessagePartitioner");
        props.put("retries", "3");
        props.put("max.in.flight.requests.per.connection", "5");
        props.put("enable.idempotence", "true");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "edu.kafka.model.ValueMessageSerializer");

        return new SimpleProducer(topic, props);
    }

    private SimpleProducer(String topic, Properties properties) {
        this.topic = topic;
        this.producer = new KafkaProducer<>(properties);
    }

    public Future<RecordMetadata> push(Message message) {
        ProducerRecord<String, Message> record = new ProducerRecord<>(topic, message.getUserId(), message);
        return Future.fromJavaFuture(producer.send(record))
                .onSuccess(metadata -> LOG.info("Pushed message {}@{}", metadata.offset(), metadata.partition()))
                .onFailure(e -> LOG.error("Failed pushing message", e));
    }

    public List<Future<RecordMetadata>> push(Message... message) {
        return List.of(message).map(this::push);
    }
}
