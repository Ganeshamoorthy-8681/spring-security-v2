package com.spring.security.dao;

import com.spring.security.domain.entity.User;
import com.spring.security.exceptions.DaoLayerException;
import java.util.List;
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
   * @param tableName the name of the table where the user data is stored
   * @param updateMap a map containing the fields to be updated, including the new password
   * @param conditionMap a map containing conditions to identify which user(s) to update
   */
  void update(String tableName, Map<String, Object> updateMap, Map<String, Object> conditionMap)
      throws DaoLayerException;

  /**
   * Retrieves a list of all users associated with a specific account ID.
   *
   * @param accountId the unique identifier of the account
   * @return a list of users associated with the given account ID
   */
  List<User> listByAccountId(Long accountId) throws DaoLayerException;

  /**
   * Deletes all role associations for a specific user.
   *
   * @param userId the ID of the user whose role associations should be deleted
   * @param accountId the ID of the account to which the user belongs
   */
  void deleteUserRoles(Long userId, Long accountId) throws DaoLayerException;

  /**
   * Assigns roles to a user by creating user-role associations.
   *
   * @param userId the ID of the user to assign roles to
   * @param roleIds the list of role IDs to assign to the user
   * @param accountId the ID of the account to which the user belongs
   */
  void assignUserRoles(Long userId, List<Long> roleIds, Long accountId) throws DaoLayerException;

  /**
   * Retrieves a root user by their account ID.
   *
   * @param accountId the account ID of the root user to retrieve
   * @return the root user with the specified account ID, or null if not found
   */
  User findRootUserByAccountId(Long accountId) throws DaoLayerException;
}
