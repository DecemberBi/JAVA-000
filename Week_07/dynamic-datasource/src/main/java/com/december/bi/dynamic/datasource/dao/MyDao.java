package com.december.bi.dynamic.datasource.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyDao {

    String query();

}
