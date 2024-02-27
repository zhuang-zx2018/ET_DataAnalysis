package com.gagc.dataanalysis.controller;

import com.alibaba.fastjson.JSON;
import com.gagc.dataanalysis.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author nyk
 * @Date 2021/11/2 17:18
 * @Version 1.0
 * @Desc 模拟kafka请求数据
 */
@RestController
@Slf4j
@RequestMapping("/kafka")
public class ProduceController {
    @Autowired
    private KafkaTemplate kafkaTemplate;//在kafakaproducerconfig配置文件中已经初始化了，这里直接拿来使用
    //@Autowired
    // private KafkaProducerListener producerListener;//发送者的监听器
    @Value("${kafka.producer.topic}")
    private String topicName;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public Object sendKafka(HttpServletRequest request) {
        String sendJson = "";
        try {
            String message = request.getParameter("message");
            log.info("kafka的消息={}", message);

//             kafkaTemplate.setProducerListener(producerListener);//设置发送者的监听器
            kafkaTemplate.send(topicName, "keyTest", getMessageJson(message));
            log.info("发送kafka成功.");
            return sendJson;
        } catch (Exception e) {
            log.error("发送kafka失败", e);
            return sendJson;
        }
    }


    String getMessageJson(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("platform", message);
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
        map.put("app_id", message);
        map.put("app_name", message);
        map.put("app_version", "6.0.1");
        map.put("car_brand", "10");
        map.put("car_vin", "LNAA3AA16L5424872");
        map.put("device_brand", "GAC");
        map.put("device_id", "A2G3SG3111LB2308EJ05");
        map.put("device_os_name", "A26.AVNT");
        map.put("device_os_ver", "A26.AVNT.S.200424.V2.52.R");
        map.put("event_code", "system_result");
        map.put("error", "123error1111111111112312");
        return JSON.toJSONString(map);
    }
}
