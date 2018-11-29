package com.hdp2;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.common.Context;

/**
 * This way use a random GROUP_ID, and set AUTO_OFFSET_RESET_CONFIG as "earliest"
 * to make Kafka recognize we as a new CONSUMER. Thus, Kafka poll data will from offset 0.
 * <p>
 * If unfortunately the GROUP_ID duplicated, this trick will not work.
 *
 * @author Dante
 */
public class ShowAllMessageEasyWay {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Context.BOOTSTRAP_SERVERS_CONFIG);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "fetch-all-consumer-" + ThreadLocalRandom.current().nextInt());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(props);
		TestConsumerRebalanceListener rebalanceListener = new TestConsumerRebalanceListener();
		consumer.subscribe(Collections.singletonList(Context.TOPIC), rebalanceListener); //asynchronous method

		ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofSeconds(5));
		for (ConsumerRecord<byte[], byte[]> record : records) {
			System.out.printf("[%s]{%s} offset:%d, [K]%s, [V]%s\n",
					record.topic(), record.partition(), record.offset(), record.key(), record.value());
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
