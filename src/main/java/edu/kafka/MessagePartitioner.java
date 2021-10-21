package edu.kafka;

import edu.kafka.model.Message;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.Random;

import static edu.kafka.KafkaApplication.MESSAGE_TOPIC;

//Assigns ERROR type messages to top (0) partition
public class MessagePartitioner implements Partitioner {

    private final Random random = new Random();

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int availablePartitions = cluster.availablePartitionsForTopic(MESSAGE_TOPIC).size();
        if (availablePartitions == 1 || ((Message) value).getType() == Message.Type.ERROR) {
            return 0;
        }
        return random.nextInt(availablePartitions - 1) + 1;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
