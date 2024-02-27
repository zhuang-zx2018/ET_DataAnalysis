package com.gagc.dataanalysis.consumer;

import com.alibaba.fastjson.JSON;
import com.gagc.dataanalysis.entity.CarBehaviorEntity;
import com.gagc.dataanalysis.entity.EventValueEntity;
import com.gagc.dataanalysis.service.CarBehaviorService;
import com.gagc.dataanalysis.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author nyk
 * @Date 2021/11/2 16:03
 * @Version 1.0
 * @Desc 消费topic的具体数据内容
 */
@Component
@Slf4j
public class KafkaConsumer {
    @Autowired
    MailService mailService;
    @Autowired
    CarBehaviorService carBehaviorService;

    @KafkaListener(topics = {"${kafka.consumer.topic}"}, containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<?, ?> record) {
        String message = record.value().toString();
//        log.info("接受到的topic值是：" + message);

        CarBehaviorEntity entity = JSON.parseObject(message, CarBehaviorEntity.class);
        if (!StringUtils.isEmpty(entity.getEvent_value())) {
            // 如果字段event_value不为空，则取出json字段，封装到对应的实体集合中去
            EventValueEntity valueEntity = JSON.parseObject(entity.getEvent_value(), EventValueEntity.class);
            entity.setSession_time(valueEntity.getSession_time());
            entity.setSystem_result(valueEntity.getSystem_result());
            entity.setApp_start(valueEntity.getApp_start());
        }
        try {
            if (!ExcludAppId().contains(entity.getApp_id()))
                carBehaviorService.save(entity);
        } catch (Exception exception) {
            mailService.sendEmail("kafka消费异常", exception.getMessage() + "\n topic的内容是：\n" + message);
            log.error("消费数据保存MySQL异常:" + exception.getMessage());
        }
    }

    // 要排除掉的AppId
    private List<String> ExcludAppId(){
        return new ArrayList<String>(){{
            add("com.android.launcher");
            add("gaei.ecallbcall");
            add("android");
            add("com.gaei.vehichesetting");
            add("com.gaei.planet");
            add("com.gaei.gaeihvsmsettings");
            add("gaei.bluetooth");
            add("com.ts.app.newenergy");
            add("gaei.video");
            add("com.gaei.image");
            add("com.gaei.settings");
            add("com.android.systemui");
            add("com.trumpchi.assistant.app");
        }};
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("platform", "Android 6.0.1");
        map.put("net_type", "NET_TYPE_4G");
        map.put("mcu_software_ver", "66");
        map.put("longitude", "113.53749633333334");
        map.put("latitude", "22.279046");
        map.put("language", "CH");
        map.put("info_str", "gcs-1.46.0-308-20200312-release");
        map.put("hardware_ver", "A26_10.1_B5");
        map.put("event_value", new HashMap<String, Object>() {{
            put("system_result", "shutdown");
        }});
        map.put("event_type", "100");
        map.put("event_time", "1634876897595");
        map.put("account_id", "NO_ACCOUNT_ID");
        map.put("ai_in_car_id", "gac_default");
        map.put("app_id", "android");
        map.put("app_name", "系统");
        map.put("app_version", "6.0.1");
        map.put("car_brand", "10");
        map.put("car_vin", "LNAA3AA16L5424872");
        map.put("device_brand", "GAC");
        map.put("device_id", "A2G3SG3111LB2308EJ05");
        map.put("device_os_name", "A26.AVNT");
        map.put("device_os_ver", "A26.AVNT.S.200424.V2.52.R");
        map.put("event_code", "system_result");
        map.put("error", "error111111111");

        System.out.println(JSON.toJSONString(map));
    }
}
