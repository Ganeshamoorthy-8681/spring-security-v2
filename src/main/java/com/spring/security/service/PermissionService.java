package com.spring.security.service;

import com.spring.security.domain.entity.Permission;
import com.spring.security.exceptions.ServiceLayerException;
import java.util.List;

/**
 * PermissionService interface for managing permissions. Provides methods to list all permissions.
 */
public interface PermissionService {

  /**
   * Lists all permissions.
   *
   * @return a list of all permissions
   */
  List<Permission> list() throws ServiceLayerException;
}
