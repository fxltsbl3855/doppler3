package com.sinoservices;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class TestKafka {
    private static final Logger logger = LoggerFactory.getLogger(TestKafka.class);

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer;
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.0.88:9092");
        props.put("group.id", "parsegroup");
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms","1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("beats"));

        while (1==1) {
            ConsumerRecords<String, String> records = consumer.poll(500);
            for (ConsumerRecord<String, String> record : records) {
                logger.debug("get kafka msg, offset = {}, value = {}", record.offset(), record.value());
            }
            logger.debug("over!!!!!!!!!!!!!!!!!!!!!__"+records.count());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }







    }
}
