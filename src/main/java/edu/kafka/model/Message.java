package edu.kafka.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {

    private String userId;
    private String text;
    private Type type;
    private LocalDateTime timestamp;

    public Message() {
    }

    public Message(String userId, String text, Type type, LocalDateTime timestamp) {
        this.userId = userId;
        this.text = text;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Type getType() {
        return type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public enum Type {
        INFO, ERROR, WARNING, TRACE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!Objects.equals(userId, message.userId)) return false;
        if (!Objects.equals(text, message.text)) return false;
        if (type != message.type) return false;
        return Objects.equals(timestamp, message.timestamp);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userId='" + userId + '\'' +
                ", text='" + text + '\'' +
                ", type=" + type +
                ", timestamp=" + timestamp +
                '}';
    }
}
