package com.spring.security.service;

import com.spring.security.controller.dto.request.RoleCreateRequestDto;
import com.spring.security.controller.dto.request.RoleUpdateRequestDto;
import com.spring.security.domain.entity.Role;
import com.spring.security.exceptions.ServiceLayerException;
import java.util.List;

/**
 * RoleService interface for managing roles in the application. Provides methods to create roles and
 * retrieve a list of roles by account ID.
 */
public interface RoleService {

  /**
   * Creates a new role based on the provided request data.
   *
   * @param roleCreateRequestDto the data transfer object containing role creation details
   * @param accountId the unique identifier of the account to which the role belongs
   */
  Role create(RoleCreateRequestDto roleCreateRequestDto, Long accountId)
      throws ServiceLayerException;

  /**
   * Retrieves a list of roles associated with a specific account ID.
   *
   * @param accountId the unique identifier of the account
   * @return a list of roles associated with the given account ID
   */
  List<Role> list(Long accountId) throws ServiceLayerException;

  /**
   * Finds a role by its unique identifier and the account ID it belongs to.
   *
   * @param roleId the unique identifier of the role
   * @param accountId the unique identifier of the account to which the role belongs
   * @return the role response data transfer object if found, otherwise null
   */
  Role findById(Long roleId, Long accountId) throws ServiceLayerException;

  /**
   * Finds a role by its name and the account ID it belongs to.
   *
   * @param roleName the name of the role
   * @param accountId the unique identifier of the account to which the role belongs
   * @return the role response data transfer object if found, otherwise null
   */
  Role findByName(String roleName, Long accountId) throws ServiceLayerException;

  /**
   * Updates an existing role with new name, description, and permissions.
   *
   * @param roleId the unique identifier of the role to be updated
   * @param accountId the unique identifier of the account to which the role belongs
   * @param roleUpdateRequestDto the data transfer object containing updated role details
   * @return the updated role
   */
  Role update(Long roleId, Long accountId, RoleUpdateRequestDto roleUpdateRequestDto)
      throws ServiceLayerException;

  /**
   * Deletes a role by its unique identifier and account ID.
   *
   * @param roleId the unique identifier of the role to be deleted
   * @param accountId the unique identifier of the account to which the role belongs
   */
  void delete(Long roleId, Long accountId) throws ServiceLayerException;
}
