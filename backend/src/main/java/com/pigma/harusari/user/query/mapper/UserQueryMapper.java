package com.pigma.harusari.user.query.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserQueryMapper {

    List<Long> getAllActiveUserIds();
}
