package edu.kafka;

import edu.kafka.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class KafkaApplication implements CommandLineRunner {

    public static final String MESSAGE_TOPIC = "message";

    private static final Logger LOG = LoggerFactory.getLogger(KafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }

    @Override
    public void run(String... args) {
        SimpleConsumer consumer1 = SimpleConsumer.create("g1", MESSAGE_TOPIC);
        SimpleConsumer consumer2 = SimpleConsumer.create("g1", MESSAGE_TOPIC);
        SimpleConsumer consumer3 = SimpleConsumer.create("g2", MESSAGE_TOPIC);

        consumer1.startPooling(message -> LOG.info("Consumer1: " + message));
        consumer2.startPooling(message -> LOG.info("Consumer2: " + message));
        consumer3.startPooling(message -> LOG.info("Consumer3: " + message));


        SimpleProducer producer = SimpleProducer.create(MESSAGE_TOPIC);
        Message m1 = new Message("user1", "aa", Message.Type.INFO, LocalDateTime.now());
        Message m2 = new Message("user1", "bb", Message.Type.ERROR, LocalDateTime.now());
        Message m3 = new Message("user1", "cc", Message.Type.ERROR, LocalDateTime.now());
        Message m4 = new Message("user1", "dd", Message.Type.WARNING, LocalDateTime.now());

        producer.push(m1, m2, m3, m4);
    }
}
