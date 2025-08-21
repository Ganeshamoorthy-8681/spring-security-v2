package com.spring.security.dao.mapper;

import com.spring.security.domain.entity.Permission;
import com.spring.security.domain.entity.Role;
import java.util.List;
import org.apache.ibatis.annotations.*;

/**
 * RoleMapper interface for handling role-related database operations. It includes methods for
 * creating roles, inserting permissions, and retrieving roles and their permissions.
 */
@Mapper
public interface RoleMapper {

  @Select(
      "INSERT INTO roles (name, description, account_id) VALUES (#{name}, #{description}, #{accountId}) RETURNING *")
  @ResultMap("roleResultMap")
  Role create(Role role);

  // Batch insert permissions for the role
  @Insert({
    "<script>",
    "INSERT INTO role_permissions (role_id, permission_id, account_id) VALUES ",
    "<foreach collection='permissionIds' item='pid' separator=','>",
    "(#{roleId}, #{pid}, #{accountId})",
    "</foreach>",
    "</script>"
  })
  int insertRolePermissions(Long roleId, List<Long> permissionIds, Long accountId);

  @Select("SELECT * FROM roles WHERE id = #{id} AND account_id = #{accountId}")
  @Results(
      id = "roleResultMap",
      value = {
        @Result(property = "id", column = "id", id = true, javaType = Long.class),
        @Result(property = "name", column = "name", javaType = String.class),
        @Result(property = "description", column = "description", javaType = String.class),
        @Result(property = "accountId", column = "account_id", javaType = Long.class),
        @Result(
            property = "permissions",
            column = "id",
            many = @Many(select = "getPermissionsByRoleId"))
      })
  Role findByAccountIdAndId(Long id, Long accountId);

  @Select(
      "SELECT p.id, p.name, p.description FROM permissions p "
          + "INNER JOIN role_permissions rp ON p.id = rp.permission_id "
          + "WHERE rp.role_id = #{roleId}")
  List<Permission> getPermissionsByRoleId(Long roleId);

  // Root role is not included in the list used internally for ROOT LEVEL access control
  @Select("SELECT * FROM roles WHERE account_id = #{accountId} AND name != 'ROOT' ")
  @ResultMap("roleResultMap")
  List<Role> listByAccountId(Long accountId);

  @Select("SELECT * FROM roles WHERE name = #{name} AND account_id = #{accountId}")
  @ResultMap("roleResultMap")
  Role findByNameAndAccountId(String name, Long accountId);
}
