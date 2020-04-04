import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {
    public static void main(String[] args) {
        String bootstrapServers = "127.0.0.1:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("myfirst", "Using Callbacks");
        kafkaProducer.send(producerRecord, new Callback() {
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                Logger logger = LoggerFactory.getLogger(Producer.class);
                if (e == null) {
                    logger.info("Successfully recieved the details");
                    logger.info("Topic " + recordMetadata.topic());
                    logger.info("Topic " + recordMetadata.timestamp());
                } else {
                    logger.error("Something went wrong" + e);
                }
            }
        });
        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
