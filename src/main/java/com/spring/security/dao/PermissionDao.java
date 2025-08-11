package com.spring.security.dao;

import com.spring.security.domain.entity.Permission;
import com.spring.security.exceptions.DaoLayerException;

import java.util.List;

/**
 * PermissionDao interface for managing permissions in the application. It provides methods to list
 * all permissions.
 */
public interface PermissionDao {

  /**
   * Lists all permissions available in the system.
   *
   * @return a list of all permissions
   */
  List<Permission> list() throws DaoLayerException;
}
