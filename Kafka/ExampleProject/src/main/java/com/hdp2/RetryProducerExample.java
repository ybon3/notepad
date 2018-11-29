package com.hdp2;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.common.Context;

import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Reference:
 * https://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.6.5/bk_kafka-component-guide/content/ch_kafka-development.html
 */
public class RetryProducerExample {

	private static TestCallback callback = new TestCallback();
	private static ProducerRecord<String, String> data = new ProducerRecord<>(
			Context.TOPIC, "message-" + ThreadLocalRandom.current().nextInt());

	public static void main(String[] args){

		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Context.BOOTSTRAP_SERVERS_CONFIG);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<>(props);
		TestCallback callback = new TestCallback();

		producer.send(data, callback);

		producer.close();
	}

	private static void send(Producer<String, String> producer) {
		producer.send(data, callback);
	}

	private static class TestCallback implements Callback {
		@Override
		public void onCompletion(RecordMetadata recordMetadata, Exception e) {
			if (e != null) {
				System.out.println("Error while producing message to topic :" + recordMetadata);
				e.printStackTrace();
			} else {
				String message = String.format("[%s]{%s} offset:%s",
						recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
				System.out.println(message);
			}
		}
	}
}
