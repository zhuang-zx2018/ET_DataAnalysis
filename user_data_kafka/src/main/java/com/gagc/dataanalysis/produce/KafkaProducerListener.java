package com.gagc.dataanalysis.produce;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * @Author nyk
 * @Date 2021/11/2 16:12
 * @Version 1.0
 * @Desc 生产者监听器
 */
@Component
@Slf4j
public class KafkaProducerListener implements ProducerListener<String, String> {
    /**
     * 成功回调
     * @return void
     */
    @Override
    public void onSuccess(String s, Integer integer, String s2, String s3, RecordMetadata recordMetadata) {
        log.info("!!!!!!!!!!!!!!!!!!!!!!!我是kafka的发送者的监听者，推送成功，推送数据：" + recordMetadata.serializedValueSize()+" s3:"+s3);
    }
    /**
     * 失败回调
     * @return void
     */
    @Override
    public void onError(String s, Integer integer, String s2, String s3, Exception e) {
        System.out.println("!!!!!!!!!!!!!!!!!!我是kafka的发送者的监听者，推送失败，推送数据：" + s3+ "，失败原因：" + e.getMessage());
    }

    @Override
    public boolean isInterestedInSuccess() {
        log.info("!!!!!!!!!!!! log我是kafka的发送者的监听者,数据发送完毕!!!!!");
        return true;
    }
}
