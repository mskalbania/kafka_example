package edu.kafka.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.control.Try;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValueMessageDeserializer implements Deserializer<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(ValueMessageDeserializer.class);

    @Override
    public Message deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return Try.of(() -> objectMapper.readValue(new String(data), Message.class))
                .onFailure(e -> LOG.error("Message serialization failed", e))
                .get();
    }
}