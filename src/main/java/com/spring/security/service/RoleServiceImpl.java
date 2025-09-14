package com.spring.security.service;

import com.spring.security.annotation.LogActivity;
import com.spring.security.controller.dto.request.RoleCreateRequestDto;
import com.spring.security.controller.dto.request.RoleUpdateRequestDto;
import com.spring.security.dao.RoleDao;
import com.spring.security.domain.entity.Role;
import com.spring.security.domain.mapper.PermissionMapper;
import com.spring.security.domain.mapper.RoleMapper;
import com.spring.security.exceptions.DaoLayerException;
import com.spring.security.exceptions.ResourceAlreadyExistException;
import com.spring.security.exceptions.ResourceNotFoundException;
import com.spring.security.exceptions.ServiceLayerException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

  private final RoleDao roleDao;

  public RoleServiceImpl(RoleDao roleDao) {
    this.roleDao = roleDao;
  }

  /**
   * Creates a new role based on the provided request data.
   *
   * @param roleCreateRequestDto the data transfer object containing role creation details
   */
  @Override
  @LogActivity(action = "CREATE", entityType = "ROLE", description = "Role created")
  public Role create(RoleCreateRequestDto roleCreateRequestDto, Long accountId)
      throws ServiceLayerException {

    try {
      if (isRoleExists(roleCreateRequestDto.getName(), accountId)) {
        log.warn(
            "Role with name: {} already exists for account ID: {}",
            roleCreateRequestDto.getName(),
            accountId);
        throw new ResourceAlreadyExistException(
            "Role with name " + roleCreateRequestDto.getName() + " already exists");
      }

      Role convertedRole =
          RoleMapper.ROLE_MAPPER.convertCreateRequestToRole(
              roleCreateRequestDto,
              accountId,
              PermissionMapper.PERMISSION_MAPPER.convertPermissionDtoToPermission(
                  roleCreateRequestDto.getPermissions()));
      return roleDao.create(convertedRole);
    } catch (DaoLayerException e) {
      log.error("Failed to create role for account ID: {}", accountId, e);
      throw new ServiceLayerException("Failed to create role", e);
    }
  }

  /**
   * Checks if a role with the specified name exists for the given account ID.
   *
   * @param roleName the name of the role to check
   * @param accountId the unique identifier of the account
   * @return true if the role exists, false otherwise
   */
  private boolean isRoleExists(String roleName, Long accountId) {
    try {
      findByName(roleName, accountId);
      return true;
    } catch (ServiceLayerException e) {
      return false;
    }
  }

  /**
   * Retrieves a list of roles associated with a specific account ID.
   *
   * @param accountId the unique identifier of the account
   * @return a list of roles associated with the given account ID
   */
  @Override
  public List<Role> list(Long accountId) throws ServiceLayerException {
    try {
      return roleDao.list(accountId);
    } catch (DaoLayerException e) {
      log.error("Failed to retrieve roles for account ID: {}", accountId, e);
      throw new ServiceLayerException("Failed to retrieve roles", e);
    }
  }

  /**
   * Finds a role by its ID and account ID.
   *
   * @param roleId the unique identifier of the role
   * @param accountId the unique identifier of the account
   * @return RoleResponseDto containing the role details
   */
  @Override
  public Role findById(Long roleId, Long accountId) throws ServiceLayerException {

    try {
      Role role = roleDao.findById(roleId, accountId);

      if (role == null) {
        log.warn("Role with ID: {} not found for account ID: {}", roleId, accountId);
        throw new ResourceNotFoundException("Role not found with ID: " + roleId);
      }
      return role;

    } catch (DaoLayerException e) {
      log.error("Failed to find role with ID: {} for account ID: {}", roleId, accountId, e);
      throw new ServiceLayerException("Failed to find role", e);
    }
  }

  /**
   * Finds a role by its name and the account ID it belongs to.
   *
   * @param roleName the name of the role
   * @param accountId the unique identifier of the account to which the role belongs
   * @return the role response data transfer object if found, otherwise null
   */
  @Override
  public Role findByName(String roleName, Long accountId) throws ServiceLayerException {
    try {
      Role role = roleDao.findByName(roleName, accountId);
      if (role == null) {
        log.warn("Role with name: {} not found for account ID: {}", roleName, accountId);
        throw new ResourceNotFoundException("Role not found with name: " + roleName);
      }
      return role;
    } catch (DaoLayerException e) {
      log.error("Failed to find role with name: {} for account ID: {}", roleName, accountId, e);
      throw new ServiceLayerException("Failed to find role", e);
    }
  }

  /**
   * Updates an existing role with new name, description, and permissions.
   *
   * @param roleId the unique identifier of the role to be updated
   * @param accountId the unique identifier of the account to which the role belongs
   * @param roleUpdateRequestDto the data transfer object containing updated role details
   * @return the updated role
   */
  @Override
  @LogActivity(action = "UPDATE", entityType = "ROLE", description = "Role updated")
  public Role update(Long roleId, Long accountId, RoleUpdateRequestDto roleUpdateRequestDto)
      throws ServiceLayerException {
    try {

      // Convert permissions from DTO to entity
      var permissions =
          PermissionMapper.PERMISSION_MAPPER.convertPermissionDtoToPermission(
              roleUpdateRequestDto.getPermissions());

      // Update the role
      return roleDao.update(
          roleId,
          accountId,
          roleUpdateRequestDto.getName(),
          roleUpdateRequestDto.getDescription(),
          permissions);

    } catch (DaoLayerException e) {
      log.error("Failed to update role with ID: {} for account ID: {}", roleId, accountId, e);
      throw new ServiceLayerException("Failed to update role", e);
    }
  }

  /**
   * Deletes a role by its unique identifier and account ID.
   *
   * @param roleId the unique identifier of the role to be deleted
   * @param accountId the unique identifier of the account to which the role belongs
   */
  @Override
  @LogActivity(action = "DELETE", entityType = "ROLE", description = "Role deleted")
  public void delete(Long roleId, Long accountId) throws ServiceLayerException {
    try {
      // First verify the role exists
      findById(roleId, accountId);

      // Delete the role
      roleDao.delete(roleId, accountId);

      log.info("Successfully deleted role with ID: {} from account: {}", roleId, accountId);
    } catch (DaoLayerException e) {
      log.error("Failed to delete role with ID: {} for account ID: {}", roleId, accountId, e);
      throw new ServiceLayerException("Failed to delete role", e);
    }
  }
}
