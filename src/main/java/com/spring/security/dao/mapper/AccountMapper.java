package com.spring.security.dao.mapper;

import com.spring.security.dao.UpdateQueryBuilder;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.domain.entity.enums.AccountType;
import com.spring.security.type.handlers.JsonTypeHandler;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

/** */
@Mapper
public interface AccountMapper {

  @Insert(
      "INSERT INTO accounts (name, status, description, type, additional_attributes) VALUES ("
          + "#{name}, #{status}, #{description}, #{type},"
          + " #{additionalAttributes, typeHandler=com.spring.security.type.handlers.JsonTypeHandler})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int create(Account account);

  @Results(
      id = "accountMap",
      value = {
        @Result(property = "id", column = "id", javaType = Long.class),
        @Result(property = "name", column = "name", javaType = String.class),
        @Result(property = "description", column = "description", javaType = String.class),
        @Result(property = "status", column = "status", javaType = AccountStatus.class),
        @Result(property = "type", column = "type", javaType = AccountType.class),
        @Result(
            property = "additionalAttributes",
            column = "additional_attributes",
            javaType = Map.class,
            typeHandler = JsonTypeHandler.class)
      })
  @Select("SELECT * FROM accounts WHERE id = #{id}")
  Account findById(Long id);

  @Select("SELECT * FROM accounts WHERE name = #{name}")
  @ResultMap("accountMap")
  Account findByName(String name);

  @UpdateProvider(type = UpdateQueryBuilder.class, method = "update")
  int update(String tableName, Map<String, Object> updates, Map<String, Object> conditions);
}
