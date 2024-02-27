package com.gagc.dataanalysis.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

/**
 * @Author nyk
 * @Date 2021/11/3 14:53
 * @Version 1.0
 * @Desc
 */
@Data
@TableName("car_behavior_info")
public class CarBehaviorEntity {
    @TableId
    private String id;
    private String account_id;
    private String ai_in_car_id;
    private String app_id;
    private String app_name;
    private String app_version;
    private String car_brand;
    private String car_vin;
    private String device_brand;
    private String device_id;
    private String device_os_name;
    private String device_os_ver;
    private String event_code;
    private String event_time;
    private String event_type;
    @TableField(exist = false)
    private String event_value;
    private String hardware_ver;
    private String info_str;
    private String language;
    private String latitude;
    private String longitude;
    private String mcu_software_ver;
    private String net_type;
    private String platform;
    private String create_time;
    private String system_result;
    private String session_time;
    private String app_start;
}
