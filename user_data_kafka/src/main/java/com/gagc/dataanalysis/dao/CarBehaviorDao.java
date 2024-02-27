package com.gagc.dataanalysis.dao;

import com.gagc.dataanalysis.entity.CarBehaviorEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author nyk
 * @Date 2021/11/3 16:49
 * @Version 1.0
 * @Desc car_behavior_info表的dao
 */
@Mapper
public interface CarBehaviorDao extends com.baomidou.mybatisplus.mapper.BaseMapper<CarBehaviorEntity>{

}
