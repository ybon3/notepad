package com.hdp2;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.common.Context;

/**
 * Reference:
 * https://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.6.5/bk_kafka-component-guide/content/ch_kafka-development.html
 */
public class BasicConsumerExample {

	public static void main(String[] args) {

		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Context.BOOTSTRAP_SERVERS_CONFIG);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(props);
		TestConsumerRebalanceListener rebalanceListener = new TestConsumerRebalanceListener();
		consumer.subscribe(Collections.singletonList(Context.TOPIC), rebalanceListener);

		while (true) {
			ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofSeconds(5));
			for (ConsumerRecord<byte[], byte[]> record : records) {
				System.out.printf("[%s]{%s} offset:%d, [K]%s, [V]%s\n",
						record.topic(), record.partition(), record.offset(), record.key(), record.value());
			}

			consumer.commitSync();
		}
	}

	private static class TestConsumerRebalanceListener implements ConsumerRebalanceListener {
		@Override
		public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
			System.out.println("Called onPartitionsRevoked with partitions:" + partitions);
		}

		@Override
		public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
			System.out.println("Called onPartitionsAssigned with partitions:" + partitions);
		}
	}
}
