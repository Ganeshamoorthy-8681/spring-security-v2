package com.spring.security.dao.mapper;

import com.spring.security.domain.entity.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PermissionMapper {

  @Select("SELECT * FROM permissions")
  @Results(
      id = "resultMap",
      value = {
        @Result(property = "id", column = "id", javaType = Long.class),
        @Result(property = "name", column = "name", javaType = String.class),
        @Result(property = "description", column = "description", javaType = String.class),
        @Result(property = "action", column = "action", javaType = String.class),
        @Result(property = "createdAt", column = "created_at", javaType = java.time.Instant.class),
      })
  List<Permission> list();
}
