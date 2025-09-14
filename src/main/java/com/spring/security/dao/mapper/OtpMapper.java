package com.spring.security.dao.mapper;

import com.spring.security.dao.UpdateQueryBuilder;
import com.spring.security.domain.entity.OtpCode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface OtpMapper {

  @Insert(
      "INSERT INTO otp_codes (otp, email, created_at, expires_at) VALUES (#{otp}, #{email}, #{createdAt}, #{expiresAt})")
  int create(OtpCode otpCode);

  @Select("SELECT * FROM otp_codes WHERE email = #{email} ORDER BY created_at DESC LIMIT 1")
  @Results(
      id = "otpResultMap",
      value = {
        @Result(property = "otp", column = "otp", javaType = String.class),
        @Result(property = "email", column = "email", javaType = String.class),
        @Result(property = "createdAt", column = "created_at", javaType = Instant.class),
        @Result(property = "expiresAt", column = "expires_at", javaType = Instant.class)
      })
  OtpCode find(String email);

  @Delete("DELETE FROM otp_codes WHERE email = #{email}")
  int delete(String email);

  @UpdateProvider(type = UpdateQueryBuilder.class, method = "update")
  int update(String tableName, Map<String, Object> updates, Map<String, Object> conditions);
}
