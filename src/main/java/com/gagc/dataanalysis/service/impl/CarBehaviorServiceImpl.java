package com.gagc.dataanalysis.service.impl;

import cn.hutool.Hutool;
import com.gagc.dataanalysis.dao.CarBehaviorDao;
import com.gagc.dataanalysis.entity.CarBehaviorEntity;
import com.gagc.dataanalysis.service.CarBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author nyk
 * @Date 2021/11/3 17:14
 * @Version 1.0
 * @Desc
 */
@Service
public class CarBehaviorServiceImpl implements CarBehaviorService {
    @Autowired
    private CarBehaviorDao carBehaviorDao;

    @Override
    public int save(CarBehaviorEntity entity) {
        entity.setId(UUID.randomUUID().toString());
        entity.setCreate_time(cn.hutool.core.date.DateUtil.now());
        return carBehaviorDao.insert(entity);
    }
}
