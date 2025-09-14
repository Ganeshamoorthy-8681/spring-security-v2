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

      int rowCount = roleMapper.insertRolePermissions(createdRole.getId(), permissionIds);

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

  /**
   * Updates an existing role with new name, description, and permissions.
   *
   * @param roleId the unique identifier of the role to be updated
   * @param accountId the unique identifier of the account associated with the role
   * @param name the new name for the role
   * @param description the new description for the role
   * @param permissions the new list of permissions for the role
   * @return the updated role
   * @throws DaoLayerException if an error occurs during the operation
   */
  @Override
  @Transactional
  public Role update(
      Long roleId, Long accountId, String name, String description, List<Permission> permissions)
      throws DaoLayerException {
    try {
      // Update role basic information
      int updatedRows = roleMapper.updateRole(roleId, accountId, name, description);
      if (updatedRows == 0) {
        log.error("Failed to update role with ID: {} for account: {}", roleId, accountId);
        throw new DaoLayerException("Failed to update role - role not found or no changes made");
      }

      // Update permissions by deleting old ones and inserting new ones
      updateRolePermissions(roleId, permissions);

      // Return the updated role
      return findById(roleId, accountId);

    } catch (Exception e) {
      log.error(
          "Error updating role with ID {} for account {}: {}", roleId, accountId, e.getMessage());
      throw new DaoLayerException("Failed to update role", e);
    }
  }

  /**
   * Updates permissions for a role by deleting existing ones and inserting new ones.
   *
   * @param roleId the unique identifier of the role
   * @param permissions the new list of permissions for the role
   */
  private void updateRolePermissions(Long roleId, List<Permission> permissions)
      throws DaoLayerException {
    try {
      // Delete existing permissions
      roleMapper.deleteRolePermissions(roleId);

      // Insert new permissions if any
      if (!CollectionUtils.isEmpty(permissions)) {
        List<Long> permissionIds = permissions.stream().map(Permission::getId).toList();
        int insertedRows = roleMapper.insertRolePermissions(roleId, permissionIds);

        if (insertedRows < permissions.size()) {
          throw new DaoLayerException("Failed to insert all permissions for role");
        }
      }
    } catch (Exception e) {
      log.error("Error updating permissions for role {}: {}", roleId, e.getMessage());
      throw new DaoLayerException("Failed to update role permissions", e);
    }
  }

  /**
   * Deletes a role by its unique identifier and account ID.
   *
   * @param roleId the unique identifier of the role to be deleted
   * @param accountId the unique identifier of the account associated with the role
   * @throws DaoLayerException if an error occurs during the operation
   */
  @Override
  @Transactional(rollbackFor = DaoLayerException.class)
  public void delete(Long roleId, Long accountId) throws DaoLayerException {
    try {
      // First delete all role-permission associations
      roleMapper.deleteRolePermissions(roleId);

      // Then delete all user-role associations (if any exist)
      // This prevents orphaned foreign key references
     // roleMapper.deleteUserRoleAssociations(roleId,accountId);

      // Finally delete the role itself
      int deletedRows = roleMapper.deleteRole(roleId, accountId);

      if (deletedRows == 0) {
        log.error("Failed to delete role with ID: {} for account: {}", roleId, accountId);
        throw new DaoLayerException("Failed to delete role - role not found");
      }

      log.info("Successfully deleted role with ID: {} from account: {}", roleId, accountId);

    } catch (Exception e) {
      log.error("Error deleting role with ID {} for account {}: {}", roleId, accountId, e.getMessage());
      throw new DaoLayerException("Failed to delete role", e);
    }
  }
}
