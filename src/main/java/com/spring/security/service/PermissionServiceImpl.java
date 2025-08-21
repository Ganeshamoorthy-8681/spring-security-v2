package com.spring.security.service;

import com.spring.security.controller.dto.response.PermissionResponseDto;
import com.spring.security.dao.PermissionDao;
import com.spring.security.domain.mapper.PermissionMapper;
import com.spring.security.exceptions.DaoLayerException;
import com.spring.security.exceptions.ServiceLayerException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** PermissionService for managing permissions */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

  private final PermissionDao permissionDao;

  public PermissionServiceImpl(PermissionDao permissionDao) {
    this.permissionDao = permissionDao;
  }

  /**
   * Lists all permissions.
   *
   * @return a list of all permissions
   */
  @Override
  public List<PermissionResponseDto> list() throws ServiceLayerException {

    try {
      return PermissionMapper.PERMISSION_MAPPER.convertPermissionToPermissionResponseDto(
          permissionDao.list());
    } catch (DaoLayerException e) {
      log.warn("Failed to list permissions", e);
      throw new ServiceLayerException("Failed to list permissions", e);
    }
  }
}
