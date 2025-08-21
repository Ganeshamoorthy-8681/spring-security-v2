package com.spring.security.dao;

import com.spring.security.domain.entity.User;
import com.spring.security.exceptions.DaoLayerException;
import java.util.Map;

public interface UserDao {

  /**
   * Creates a new user in the database.
   *
   * @param user the user to be created
   */
  User create(User user) throws DaoLayerException;

  /**
   * Retrieves a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return the user with the specified ID, or null if not found
   */
  User findById(Long accountId, Long id) throws DaoLayerException;

  /**
   * Retrieves a user by their email.
   *
   * @param email the email of the user to retrieve
   * @return the user with the specified email, or null if not found
   */
  User findByAccountIdAndEmail(Long accountId, String email) throws DaoLayerException;

  /**
   * Retrieves a root user by their email.
   *
   * @param email the email of the user to retrieve
   * @return the user with the specified email, or null if not found
   */
  User findByEmail(String email) throws DaoLayerException;

  /**
   * Updates the password of a user based on the provided update and condition maps.
   *
   * @param updateMap a map containing the fields to be updated, including the new password
   * @param conditionMap a map containing conditions to identify which user(s) to update
   */
  void update(Map<String, Object> updateMap, Map<String, Object> conditionMap)
      throws DaoLayerException;
}
