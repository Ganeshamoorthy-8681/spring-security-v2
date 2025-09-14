package com.spring.security.dao.mapper;

import com.spring.security.domain.entity.ActivityLog;
import java.util.List;
import org.apache.ibatis.annotations.*;

/** Mapper interface for ActivityLog database operations. */
@Mapper
public interface ActivityLogMapper {
  @Insert(
      "INSERT INTO activity_logs (user_email, account_id, action, entity_type, entity_id, description, ip_address, user_agent, created_at) VALUES (#{userEmail}, #{accountId}, #{action}, #{entityType}, #{entityId}, #{description}, #{ipAddress}, #{userAgent}, #{createdAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(ActivityLog log);

  @Results(
      id = "activityLogResultMap",
      value = {
        @Result(property = "id", column = "id", javaType = Long.class),
        @Result(property = "userId", column = "user_id", javaType = Long.class),
        @Result(property = "accountId", column = "account_id", javaType = Long.class),
        @Result(property = "action", column = "action", javaType = String.class),
        @Result(property = "entityType", column = "entity_type", javaType = String.class),
        @Result(property = "entityId", column = "entity_id", javaType = Long.class),
        @Result(property = "description", column = "description", javaType = String.class),
        @Result(property = "ipAddress", column = "ip_address", javaType = String.class),
        @Result(property = "userAgent", column = "user_agent", javaType = String.class),
        @Result(property = "createdAt", column = "created_at", javaType = java.time.Instant.class)
      })
  @Select(
      "SELECT id, user_id, account_id, action, entity_type, entity_id, description, ip_address, user_agent, created_at FROM activity_logs WHERE id = #{id}")
  ActivityLog findById(@Param("id") Long id);

  @ResultMap("activityLogResultMap")
  @Select(
      "SELECT id, user_id, account_id, action, entity_type, entity_id, description, ip_address, user_agent, created_at FROM activity_logs WHERE user_id = #{userId} ORDER BY created_at DESC")
  List<ActivityLog> findByUserId(@Param("userId") Long userId);

  @ResultMap("activityLogResultMap")
  @Select(
      "SELECT id, user_id, account_id, action, entity_type, entity_id, description, ip_address, user_agent, created_at FROM activity_logs WHERE account_id = #{accountId} ORDER BY created_at DESC")
  List<ActivityLog> findByAccountId(@Param("accountId") Long accountId);
}
