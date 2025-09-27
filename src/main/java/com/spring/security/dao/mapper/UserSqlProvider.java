package com.spring.security.dao.mapper;

import org.apache.ibatis.jdbc.SQL;

public class UserSqlProvider {

  public String baseUserWithRolesAndPermissionsQuery(String whereClause) {
    String query =
        new SQL() {
          {
            SELECT(
                "u.id AS user_id, u.first_name, u.last_name, u.middle_name, u.email, u.type, u.status, u.password, u.account_id, "
                    + "u.created_at, u.updated_at, u.deleted_at, u.current_login, u.last_login, u.failed_login_attempts, u.last_failed_login, "
                    + "u.created_by, u.updated_by, u.deleted_by, u.additional_attributes, u.is_root, "
                    + "r.id AS role_id, r.name AS role_name, r.description AS role_description, "
                    + "p.id AS permission_id, p.name AS permission_name, p.description AS permission_description, p.action AS permission_action");

            FROM("users u");
            LEFT_OUTER_JOIN("user_roles ur ON u.id = ur.user_id");
            LEFT_OUTER_JOIN("roles r ON ur.role_id = r.id");
            LEFT_OUTER_JOIN("role_permissions rp ON r.id = rp.role_id");
            LEFT_OUTER_JOIN("permissions p ON rp.permission_id = p.id");

            // dynamic WHERE
            WHERE(whereClause);
          }
        }.toString();
    System.out.println(query);
    return query;
  }

  public String findByEmail(String email) {
    return baseUserWithRolesAndPermissionsQuery("u.email = #{email}");
  }

  public String findByEmailAndAccountId(Long accountId, String email) {
    return baseUserWithRolesAndPermissionsQuery(
        "u.email = #{email} AND u.account_id = #{accountId}");
  }

  public String findByIdAndAccountId(Long accountId, Long id) {
    return baseUserWithRolesAndPermissionsQuery("u.id = #{id} AND u.account_id = #{accountId}");
  }

  public String listByAccountId(Long accountId) {
    return baseUserWithRolesAndPermissionsQuery("u.account_id = #{accountId}");
  }
}
