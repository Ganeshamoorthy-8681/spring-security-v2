package com.spring.security.dao.mapper;

import com.spring.security.dao.UpdateQueryBuilder;
import com.spring.security.domain.entity.Permission;
import com.spring.security.domain.entity.Role;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.entity.enums.UserType;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

/** UserMapper interface for MyBatis to map User entity operations. */
@Mapper
public interface UserMapper {

  @Insert(
      "INSERT INTO users (first_name, last_name, middle_name, email, type, status, account_id, additional_attributes) "
          + "VALUES (#{firstName}, #{lastName}, #{middleName}, #{email}, #{type}, #{status}, #{accountId}, "
          + "#{additionalAttributes, typeHandler=com.spring.security.type.handlers.JsonTypeHandler})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int create(User user);

  // Batch insert roles for the user
  @Insert({
    "<script>",
    "INSERT INTO user_roles (user_id, role_id,account_id) VALUES ",
    "<foreach collection='roleIds' item='roleId' separator=','>",
    "(#{userId}, #{roleId}, #{accountId})",
    "</foreach>",
    "ON CONFLICT (user_id, role_id, account_id) DO NOTHING",
    "</script>"
  })
  int insertUserRoles(Long userId, List<Long> roleIds, Long accountId);

  @Select(
      "SELECT id, first_name, last_name, middle_name, email,type,account_id, password, status FROM users WHERE email = #{email} AND account_id = #{accountId}")
  @Results(
      id = "userMap",
      value = {
        @Result(property = "id", column = "id", javaType = Long.class),
        @Result(property = "firstName", column = "first_name", javaType = String.class),
        @Result(property = "lastName", column = "last_name", javaType = String.class),
        @Result(property = "middleName", column = "middle_name", javaType = String.class),
        @Result(property = "email", column = "email", javaType = String.class),
        @Result(property = "type", column = "type", javaType = UserType.class),
        @Result(property = "accountId", column = "account_id", javaType = Long.class),
        @Result(property = "status", column = "status", javaType = UserStatus.class),
        @Result(property = "password", column = "password", javaType = String.class),
        @Result(property = "roles", column = "id", many = @Many(select = "getRolesByUserId"))
      })
  User findByAccountIdAndEmail(Long accountId, String email);

  @Select(
      "SELECT r.id, r.name, r.description FROM roles r "
          + "JOIN user_roles ur ON ur.role_id = r.id "
          + "WHERE ur.user_id = #{userId}")
  @Results({
    @Result(property = "id", column = "id"),
    @Result(
        property = "permissions",
        column = "id",
        many = @Many(select = "getPermissionsByRoleId"))
  })
  List<Role> getRolesByUserId(Long userId);

  @Select(
      "SELECT p.id, p.name FROM permissions p "
          + "JOIN role_permissions rp ON rp.permission_id = p.id "
          + "WHERE rp.role_id = #{roleId}")
  List<Permission> getPermissionsByRoleId(Long roleId);

  @Select(
      "SELECT id, password, first_name,last_name,middle_name,email,type,account_id,status FROM users WHERE id = #{id} AND account_id = #{accountId}")
  @ResultMap(value = "userMap")
  User findByAccountIdAndUserId(Long accountId, Long id);

  @Select(
      "SELECT id, password, first_name, last_name, middle_name, email, type, account_id, status FROM users WHERE email = #{email}")
  @ResultMap(value = "userMap")
  User findByEmail(String email);

  @UpdateProvider(type = UpdateQueryBuilder.class, method = "update")
  int update(String tableName, Map<String, Object> updates, Map<String, Object> conditions);
}
