docker exec kafka1 sh -c "exec /opt/bitnami/kafka/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --topic message --partitions 4 --replication-factor 2"
sleep 3
docker exec kafka1 sh -c "exec /opt/bitnami/kafka/bin/kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic message"



# EXAMPLE DISTRIBUTION for 4 partitions & rep factor 2
#           Broker 11                            Broker 22                          Broker 33                             Broker 44
#+-------------------------------+    +-------------------------------+     +-------------------------------+   +-------------------------------+
#|                               |    |                               |     |                               |   |                               |
#| +--------------------------+  |    | +--------------------------+  |     | +--------------------------+  |   | +--------------------------+  |
#| |                          | <-------|                          |  |     | |                          |  |   | |                          |  |
#| |  PARTITION 3             |  |    | |    PARTITION 3 LEAD      |  |     | |                          |  |   | |                          |  |
#| |                          |  |    | |                          |  |     | |                          |  |   | |                          |  |
#| +--------------------------+  |    | +--------------------------+  |     | +--------------------------+  |   | +--------------------------+  |
#|                               |    |                               |     |                               |   |                               |
#| +--------------------------+  |    | +--------------------------+  |     | +--------------------------+  |   | +--------------------------+  |
#| |                          |  |    | |                          |  |     | |                          |  |   | |                          |  |
#| |   PARTITION 2 LEAD       | --------------------------------------------->|     PARTITION 2          |  |   | |                          |  |
#| |                          |  |    | |                          |  |     | |                          |  |   | |                          |  |
#| +--------------------------+  |    | +--------------------------+  |     | +--------------------------+  |   | +--------------------------+  |
#|                               |    |                               |     |                               |   |                               |
#| +--------------------------+  |    | +--------------------------+  |     | +--------------------------+  |   | +--------------------------+  |
#| |                          |  |    | |                          |  |     | |                          |  |   | |                          |  |
#| |                          |  |    | |                          |  |     | |   PARTITION 1 LEAD       | ------>|   PARTITION 1            |  |
#| |                          |  |    | |                          |  |     | |                          |  |   | |                          |  |
#| +--------------------------+  |    | +--------------------------+  |     | +--------------------------+  |   | +--------------------------+  |
#|                               |    |                               |     |                               |   |                               |
#| +--------------------------+  |    | +--------------------------+  |     | +--------------------------+  |   | +--------------------------+  |
#| |                          |  |    | |                          |<---------------------------------------------|                          |  |
#| |                          |  |    | |  PARTITION 0             |  |     | |                          |  |   | |    PARTITION 0  LEAD     |  |
#| |                          |  |    | |                          |  |     | |                          |  |   | |                          |  |
#| +--------------------------+  |    | +--------------------------+  |     | +--------------------------+  |   | +--------------------------+  |
#|                               |    |                               |     |                               |   |                               |
#|                               |    |                               |     |                               |   |                               |
#|                               |    |                               |     |                               |   |                               |
#+-------------------------------+    +-------------------------------+     +-------------------------------+   +-------------------------------+


