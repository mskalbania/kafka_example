package edu.kafka.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.control.Try;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class ValueMessageSerializer implements Serializer<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(ValueMessageSerializer.class);

    @Override
    public byte[] serialize(String topic, Message data) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return Try.of(() -> objectMapper.writeValueAsString(data))
                .map(s -> s.getBytes(StandardCharsets.UTF_8))
                .onFailure(e -> LOG.error("Message serialization failed", e))
                .get();
    }
}