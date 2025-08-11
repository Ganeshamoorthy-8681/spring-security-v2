package com.spring.security.dao;

import com.spring.security.domain.entity.Role;
import com.spring.security.exceptions.DaoLayerException;

import java.util.List;

/**
 * RoleDao interface for managing roles in the application. This interface can be extended to
 * include methods for role management.
 */
public interface RoleDao {

  /**
   * Creates a new role in the system.
   *
   * @param role the role to be created
   */
  Role create(Role role) throws DaoLayerException;

  /**
   * Retrieves a role by its ID.
   *
   * @param id the unique identifier of the role
   * @param accountId the account ID associated with the role
   */
  Role findById(Long id, Long accountId) throws DaoLayerException;

  /**
   * Retrieves a list of roles associated with a specific account ID.
   *
   * @param accountId the unique identifier of the account
   * @return a list of roles associated with the given account ID
   */
  List<Role> list(Long accountId) throws DaoLayerException;


  /**
   * Finds a role by its name and account ID.
   * @param name the name of the role to be found
   * @param accountId the unique identifier of the account associated with the role
   * @return the role with the specified name and account ID, or null if not found
   * @throws DaoLayerException if an error occurs during the operation
   */
  Role findByName(String name, Long accountId) throws DaoLayerException;
}
