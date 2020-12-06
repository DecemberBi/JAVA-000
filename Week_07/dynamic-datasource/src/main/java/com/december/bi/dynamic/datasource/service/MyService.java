package com.december.bi.dynamic.datasource.service;

import com.december.bi.dynamic.datasource.annotation.MyDataSource;
import com.december.bi.dynamic.datasource.dao.MyDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyService {

    @Autowired
    private MyDao myDao;

    @MyDataSource
    public String hello() {
        log.info("hello start");
        return "hello world";
    }

    @MyDataSource()
    public String master() {
       String name = myDao.query();
       log.info("master result name:{}",name);
        return name;
    }

    @MyDataSource("slave")
    public String slave() {
        String name = myDao.query();
        log.info("slave result name:{}",name);
        return name;
    }
}
