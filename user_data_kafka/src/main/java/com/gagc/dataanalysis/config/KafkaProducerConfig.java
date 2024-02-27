package com.gagc.dataanalysis.config;

import com.gagc.dataanalysis.produce.KafkaProducerListener;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author nyk
 * @Date 2021/11/2 16:10
 * @Version 1.0
 * @Desc 生产者配置、为了便于测试，所以也加载了生产者的配置
 */
@Configuration
@EnableKafka
public class KafkaProducerConfig {
    @Value("${kafka.producer.bootstrap.servers}")
    private String servers;
    @Value("${kafka.producer.topic}")
    private String topic;
    @Value("${kafka.producer.retries}")
    private int retries;
    @Value("${kafka.producer.acks}")
    private String acks;
    @Value("${kafka.producer.batch.size}")
    private int batchSize;
    @Value("${kafka.producer.linger}")
    private int linger;
    @Value("${kafka.producer.buffer.memory}")
    private int bufferMemory;
    /**
     * 加载配置信息
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }
    /**
     *  使用producer配置项对象来构建producerFactory
     * @return org.springframework.kafka.core.ProducerFactory<java.lang.String,java.lang.String>
     */
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    /**
     * 使用producerFactory来构建kafkaTemplate的bean,实例化一个KafkaTemplate对象
     * @return org.springframework.kafka.core.KafkaTemplate<java.lang.String,java.lang.String>
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> template= new KafkaTemplate<String, String>(producerFactory());
        template.setProducerListener(kafkaProducerListener());
        return template;
    }
    /**
     * 生产者的监听器
     * @return org.springframework.kafka.support.ProducerListener
     */
    @Bean
    public KafkaProducerListener kafkaProducerListener(){
        KafkaProducerListener listener = new KafkaProducerListener();
        return listener;

    }
}
