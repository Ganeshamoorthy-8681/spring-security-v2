package com.spring.security.service;

import com.spring.security.controller.dto.request.RoleCreateRequestDto;
import com.spring.security.controller.dto.response.RoleResponseDto;
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
  public RoleResponseDto create(RoleCreateRequestDto roleCreateRequestDto, Long accountId)
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
          RoleMapper.ROLE_MAPPER.convertCreteRequestToRole(
              roleCreateRequestDto,
              accountId,
              PermissionMapper.PERMISSION_MAPPER.convertPermissionDtoToPermission(
                  roleCreateRequestDto.getPermissions()));
      Role role = roleDao.create(convertedRole);
      return RoleMapper.ROLE_MAPPER.convertRoleToResponseDto(role);
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
  public List<RoleResponseDto> list(Long accountId) throws ServiceLayerException {
    try {
      return RoleMapper.ROLE_MAPPER.convertRoleListToResponseDtoList(roleDao.list(accountId));
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
  public RoleResponseDto findById(Long roleId, Long accountId) throws ServiceLayerException {

    try {
      Role role = roleDao.findById(roleId, accountId);

      if (role == null) {
        log.warn("Role with ID: {} not found for account ID: {}", roleId, accountId);
        throw new ResourceNotFoundException("Role not found with ID: " + roleId);
      }
      return RoleMapper.ROLE_MAPPER.convertRoleToResponseDto(role);

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
  public RoleResponseDto findByName(String roleName, Long accountId) throws ServiceLayerException {
    try {
      Role role = roleDao.findByName(roleName, accountId);
      if (role == null) {
        log.warn("Role with name: {} not found for account ID: {}", roleName, accountId);
        throw new ResourceNotFoundException("Role not found with name: " + roleName);
      }
      return RoleMapper.ROLE_MAPPER.convertRoleToResponseDto(role);
    } catch (DaoLayerException e) {
      log.error("Failed to find role with name: {} for account ID: {}", roleName, accountId, e);
      throw new ServiceLayerException("Failed to find role", e);
    }
  }
}
