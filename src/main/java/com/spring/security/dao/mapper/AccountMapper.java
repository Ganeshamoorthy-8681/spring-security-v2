package com.spring.security.dao.mapper;

import com.spring.security.dao.UpdateQueryBuilder;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.entity.AccountStats;
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
      "INSERT INTO accounts (name, status, description, type, additional_attributes, created_by) VALUES ("
          + "#{name}, #{status}, #{description}, #{type},"
          + " #{additionalAttributes, typeHandler=com.spring.security.type.handlers.JsonTypeHandler},#{createdBy})")
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
            typeHandler = JsonTypeHandler.class),
        @Result(property = "createdAt", column = "created_at", javaType = java.time.Instant.class),
        @Result(property = "updatedAt", column = "updated_at", javaType = java.time.Instant.class),
        @Result(property = "createdBy", column = "created_by", javaType = String.class),
        @Result(property = "updatedBy", column = "updated_by", javaType = String.class)
      })
  @Select("SELECT * FROM accounts WHERE id = #{id}")
  Account findById(Long id);

  @Select("SELECT * FROM accounts WHERE name = #{name}")
  @ResultMap("accountMap")
  Account findByName(String name);

  @UpdateProvider(type = UpdateQueryBuilder.class, method = "update")
  int update(String tableName, Map<String, Object> updates, Map<String, Object> conditions);

  @Select(
      "SELECT "
          + "    COUNT(*) AS totalUsers, "
          + "    SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS activeUsers, "
          + "    SUM(CASE WHEN status = 'INACTIVE' THEN 1 ELSE 0 END) AS inactiveUsers, "
          + "    SUM(CASE WHEN failed_login_attempts > 0 THEN 1 ELSE 0 END) AS failedLoginAttempts, "
          + "    SUM(CASE WHEN status = 'CREATED' THEN 1 ELSE 0 END) AS pendingInvitations "
          + "   FROM users WHERE account_id = #{accountId}")
  @Results(
      id = "accountStatsMap",
      value = {
        @Result(property = "totalUsers", column = "totalUsers", javaType = Long.class),
        @Result(property = "activeUsers", column = "activeUsers", javaType = Long.class),
        @Result(property = "inactiveUsers", column = "inactiveUsers", javaType = Long.class),
        @Result(
            property = "failedLoginAttempts",
            column = "failedLoginAttempts",
            javaType = Long.class),
        @Result(
            property = "pendingInvitations",
            column = "pendingInvitations",
            javaType = Long.class)
      })
  AccountStats getAccountStats(Long accountId);
}
