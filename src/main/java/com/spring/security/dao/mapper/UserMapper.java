package com.spring.security.dao.mapper;

import com.spring.security.dao.UpdateQueryBuilder;
import com.spring.security.domain.entity.Permission;
import com.spring.security.domain.entity.Role;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.entity.enums.UserType;
import java.time.Instant;
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
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface UserMapper {

  @Insert(
      """
      INSERT INTO users (first_name, last_name, middle_name, email, type, status, account_id, additional_attributes, is_root)
      VALUES (#{firstName}, #{lastName}, #{middleName}, #{email}, #{type}, #{status}, #{accountId},
      #{additionalAttributes, typeHandler=com.spring.security.type.handlers.JsonTypeHandler}, #{isRoot})
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int create(User user);

  @Insert({
    "<script>",
    "INSERT INTO user_roles (user_id, role_id) VALUES ",
    "<foreach collection='roleIds' item='roleId' separator=','>",
    "(#{userId}, #{roleId})",
    "</foreach>",
    "ON CONFLICT (user_id, role_id) DO NOTHING",
    "</script>"
  })
  int insertUserRoles(Long userId, List<Long> roleIds);

  @SelectProvider(type = UserSqlProvider.class, method = "findByEmailAndAccountId")
  @Results(
      id = "userWithRolesAndPermissions",
      value = {
        @Result(property = "id", column = "user_id"),
        @Result(property = "firstName", column = "first_name"),
        @Result(property = "lastName", column = "last_name"),
        @Result(property = "middleName", column = "middle_name"),
        @Result(property = "email", column = "email"),
        @Result(property = "password", column = "password"),
        @Result(property = "type", column = "type", javaType = UserType.class),
        @Result(property = "status", column = "status", javaType = UserStatus.class),
        @Result(property = "accountId", column = "account_id"),
        @Result(property = "createdAt", column = "created_at", javaType = Instant.class),
        @Result(property = "updatedAt", column = "updated_at", javaType = Instant.class),
        @Result(property = "deletedAt", column = "deleted_at", javaType = Instant.class),
        @Result(property = "currentLogin", column = "current_login", javaType = Instant.class),
        @Result(property = "lastLogin", column = "last_login", javaType = Instant.class),
        @Result(property = "failedLoginAttempts", column = "failed_login_attempts"),
        @Result(
            property = "lastFailedLogin",
            column = "last_failed_login",
            javaType = Instant.class),
        @Result(property = "createdBy", column = "created_by"),
        @Result(property = "updatedBy", column = "updated_by"),
        @Result(property = "deletedBy", column = "deleted_by"),
        @Result(
            property = "additionalAttributes",
            column = "additional_attributes",
            javaType = Map.class,
            typeHandler = com.spring.security.type.handlers.JsonTypeHandler.class),
        @Result(
            property = "roles",
            javaType = List.class,
            column = "role_id",
            many = @Many(resultMap = "roleWithPermissionsResultMap")),
            @Result(property = "isRoot", column = "is_root", javaType = Boolean.class)
      })
  User findByAccountIdAndEmail(Long accountId, String email);

  @Results(
      id = "roleWithPermissionsResultMap",
      value = {
        @Result(property = "id", column = "role_id"),
        @Result(property = "name", column = "role_name"),
        @Result(property = "description", column = "role_description"),
        @Result(
            property = "permissions",
            javaType = List.class,
            column = "permission_id",
            many = @Many(resultMap = "permissionResultMap"))
      })
  @Select("")
  Role mapRolesAndPermissions();

  @Results(
      id = "permissionResultMap",
      value = {
        @Result(property = "id", column = "permission_id"),
        @Result(property = "name", column = "permission_name"),
        @Result(property = "description", column = "permission_description"),
        @Result(property = "action", column = "permission_action")
      })
  @Select("")
  Permission mapPermissions();

  @SelectProvider(type = UserSqlProvider.class, method = "findByIdAndAccountId")
  @ResultMap("userWithRolesAndPermissions")
  User findByAccountIdAndUserId(Long accountId, Long id);

  @SelectProvider(type = UserSqlProvider.class, method = "findByEmail")
  @ResultMap("userWithRolesAndPermissions")
  User findByEmail(String email);

  @UpdateProvider(type = UpdateQueryBuilder.class, method = "update")
  int update(String tableName, Map<String, Object> updates, Map<String, Object> conditions);

  @SelectProvider(type = UserSqlProvider.class, method = "listByAccountId")
  @ResultMap("userWithRolesAndPermissions")
  List<User> listByAccountId(Long accountId);

  @Update(
      """
      DELETE FROM user_roles ur
      USING users u
      WHERE ur.user_id = u.id
        AND u.id = #{userId}
        AND u.account_id = #{accountId}
      """)
  int deleteUserRoles(Long userId, Long accountId);

  @Insert(
      """
      INSERT INTO user_roles (user_id, role_id)
      SELECT #{userId}, #{roleId}
      FROM users u
      JOIN roles r ON r.account_id = u.account_id
      WHERE u.id = #{userId}
        AND r.id = #{roleId}
        AND u.account_id = #{accountId}
      ON CONFLICT (user_id, role_id) DO NOTHING
      """)
  void insertUserRole(Long userId, Long roleId, Long accountId);

  @Select("SELECT * FROM users WHERE account_id = #{accountId} AND is_root = true")
  @Results(
      value = {
        @Result(property = "id", column = "id"),
        @Result(property = "firstName", column = "first_name"),
        @Result(property = "lastName", column = "last_name"),
        @Result(property = "middleName", column = "middle_name"),
        @Result(property = "email", column = "email"),
        @Result(property = "type", column = "type", javaType = UserType.class),
        @Result(property = "status", column = "status", javaType = UserStatus.class),
        @Result(property = "createdAt", column = "created_at", javaType = Instant.class),
        @Result(property = "updatedAt", column = "updated_at", javaType = Instant.class),
        @Result(property = "deletedAt", column = "deleted_at", javaType = Instant.class),
        @Result(property = "currentLogin", column = "current_login", javaType = Instant.class),
        @Result(property = "lastLogin", column = "last_login", javaType = Instant.class),
        @Result(property = "failedLoginAttempts", column = "failed_login_attempts")
      })
  User findRootUserByAccountId(Long accountId);
}
