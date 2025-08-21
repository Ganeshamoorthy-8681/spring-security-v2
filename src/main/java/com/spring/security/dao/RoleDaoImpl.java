package com.spring.security.dao;

import com.spring.security.dao.mapper.RoleMapper;
import com.spring.security.domain.entity.Permission;
import com.spring.security.domain.entity.Role;
import com.spring.security.exceptions.DaoLayerException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Implementation of RoleDao for managing roles in the system. This class interacts with the
 * RoleMapper to perform database operations related to roles.
 */
@Slf4j
@Component
public class RoleDaoImpl implements RoleDao {

  private final RoleMapper roleMapper;

  /**
   * Constructor for RoleDaoImpl.
   *
   * @param roleMapper the mapper to handle role-related database operations
   */
  public RoleDaoImpl(RoleMapper roleMapper) {
    this.roleMapper = roleMapper;
  }

  /**
   * Creates a new role in the system.
   *
   * @param role the role to be created
   */
  @Override
  @Transactional
  public Role create(Role role) throws DaoLayerException {

    try {
      Role createdRole = roleMapper.create(role);
      if (createdRole == null) {
        log.error("Failed to create role: {}", role.getName());
        throw new DaoLayerException("Failed to create role");
      }
      role.setId(createdRole.getId());
      insertRolePermissions(role);
      return createdRole;
    } catch (Exception e) {
      log.error("Error creating role: {}", e.getMessage());
      throw new DaoLayerException("Failed to create role", e);
    }
  }

  /**
   * Inserts permissions for a newly created role.
   *
   * @param createdRole the role for which permissions are to be inserted
   */
  private void insertRolePermissions(Role createdRole) throws DaoLayerException {

    try {
      if (CollectionUtils.isEmpty(createdRole.getPermissions())) {
        log.warn("No permissions provided for role: {}", createdRole.getName());
        return;
      }

      List<Long> permissionIds =
          createdRole.getPermissions().stream().map(Permission::getId).toList();

      int rowCount =
          roleMapper.insertRolePermissions(
              createdRole.getId(), permissionIds, createdRole.getAccountId());

      if (rowCount < 1) {
        throw new DaoLayerException(
            "Failed to insert permissions for role: " + createdRole.getName());
      }

    } catch (Exception e) {
      log.error(
          "Error inserting permissions for role {}: {}", createdRole.getName(), e.getMessage());
      throw new DaoLayerException("Failed to insert permissions for role", e);
    }
  }

  /**
   * Retrieves a role by its ID.
   *
   * @param id the unique identifier of the role
   */
  @Override
  public Role findById(Long id, Long accountId) throws DaoLayerException {

    try {
      return roleMapper.findByAccountIdAndId(id, accountId);
    } catch (Exception e) {
      log.error("Error retrieving role with ID {}: {}", id, e.getMessage());
      throw new DaoLayerException("Failed to retrieve role", e);
    }
  }

  /**
   * Retrieves a list of roles associated with a specific account ID.
   *
   * @param accountId the unique identifier of the account
   * @return a list of roles associated with the given account ID
   */
  @Override
  public List<Role> list(Long accountId) throws DaoLayerException {

    try {
      return roleMapper.listByAccountId(accountId);
    } catch (Exception e) {
      log.error("Error retrieving roles for account {}: {}", accountId, e.getMessage());
      throw new DaoLayerException("Failed to retrieve roles", e);
    }
  }

  /**
   * Finds a role by its name and account ID.
   *
   * @param name the name of the role to be found
   * @param accountId the unique identifier of the account associated with the role
   * @return the role with the specified name and account ID, or null if not found
   * @throws DaoLayerException if an error occurs during the operation
   */
  @Override
  public Role findByName(String name, Long accountId) throws DaoLayerException {
    try {
      return roleMapper.findByNameAndAccountId(name, accountId);
    } catch (Exception e) {
      log.error(
          "Error retrieving role with name {} for account {}: {}", name, accountId, e.getMessage());
      throw new DaoLayerException("Failed to retrieve role by name", e);
    }
  }
}
