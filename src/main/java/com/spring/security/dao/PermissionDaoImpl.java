package com.spring.security.dao;

import com.spring.security.dao.mapper.PermissionMapper;
import com.spring.security.domain.entity.Permission;
import java.util.List;

import com.spring.security.exceptions.DaoLayerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Implementation of the PermissionDao interface for managing permissions. This class interacts with
 * the database through the PermissionMapper.
 */
@Component
@Slf4j
public class PermissionDaoImpl implements PermissionDao {

  private final PermissionMapper permissionMapper;

  /**
   * Constructor for PermissionDaoImpl.
   *
   * @param permissionMapper the mapper to interact with the database
   */
  PermissionDaoImpl(PermissionMapper permissionMapper) {
    this.permissionMapper = permissionMapper;
  }

  /**
   * List all permissions.
   *
   * @return an empty list as this is a placeholder implementation
   */
  @Override
  public List<Permission> list() throws DaoLayerException {
    try {
      return permissionMapper.list();
    }catch (Exception e){
        log.warn("Error while listing permissions", e);
        throw new DaoLayerException("Error while listing permissions",e);
    }
  }
}
