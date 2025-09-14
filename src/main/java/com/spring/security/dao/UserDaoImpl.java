package com.spring.security.dao;

import com.spring.security.dao.mapper.UserMapper;
import com.spring.security.domain.entity.Role;
import com.spring.security.domain.entity.User;
import com.spring.security.exceptions.DaoLayerException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserDaoImpl implements UserDao {

  private final UserMapper userMapper;

  /**
   * Constructor for UserDaoImpl.
   *
   * @param userMapper the UserMapper to be used for database operations
   */
  public UserDaoImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  /**
   * Creates a new user in the database.
   *
   * @param user the user to be created
   */
  @Override
  @Transactional(rollbackFor = DaoLayerException.class)
  public User create(User user) throws DaoLayerException {
    createUserInDb(user);
    assignRoles(user);
    return user;
  }

  /**
   * Creates a new user in the database.
   *
   * @param user the user to be created
   * @throws DaoLayerException if there is an error during the database operation
   */
  private void createUserInDb(User user) throws DaoLayerException {

    try {
      int createdRowCount = userMapper.create(user);

      if (createdRowCount <= 0) {
        log.warn("Failed to create user: {}", user.getEmail());
        throw new DaoLayerException("Failed to create user");
      }

    } catch (Exception e) {
      log.error("Error creating user {}: {}", user.getEmail(), e.getMessage());
      throw new DaoLayerException("Failed to create user", e);
    }
  }

  /**
   * Assigns roles to the user in the database.
   *
   * @param user the user whose roles are to be assigned
   * @throws DaoLayerException if there is an error during the database operation
   */
  private void assignRoles(User user) throws DaoLayerException {

    try {

      List<Long> roleIds =
          Optional.ofNullable(user.getRoles()).orElse(Collections.emptyList()).stream()
              .map(Role::getId)
              .toList();

      if (roleIds.isEmpty()) {
        log.warn("No roles provided for user: {}", user.getEmail());
        return;
      }
      int rowCount = userMapper.insertUserRoles(user.getId(), roleIds);
      if (rowCount <= 0) {
        log.warn("No roles were inserted for user {}", user.getEmail());
        throw new DaoLayerException("Failed to create user");
      }
    } catch (Exception e) {
      log.error("Error inserting roles for user {}: {}", user.getEmail(), e.getMessage());
      throw new DaoLayerException("Failed to assign roles to user", e);
    }
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return the user with the specified ID, or null if not found
   */
  @Override
  public User findById(Long accountId, Long id) throws DaoLayerException {

    try {
      return userMapper.findByAccountIdAndUserId(accountId, id);
    } catch (Exception e) {
      log.error(
          "Error retrieving user with ID {} for account {}: {}", id, accountId, e.getMessage());
      throw new DaoLayerException("Failed to retrieve user", e);
    }
  }

  /**
   * Retrieves a user by their email.
   *
   * @param email the email of the user to retrieve
   * @return the user with the specified email, or null if not found
   */
  @Override
  public User findByAccountIdAndEmail(Long accountId, String email) throws DaoLayerException {

    try {
      return userMapper.findByAccountIdAndEmail(accountId, email);
    } catch (Exception e) {
      log.error(
          "Error retrieving user with email {} for account {}: {}",
          email,
          accountId,
          e.getMessage());
      throw new DaoLayerException("Failed to retrieve user by email", e);
    }
  }

  /**
   * Retrieves a root user by their email.
   *
   * @param email the email of the user to retrieve
   * @return the user with the specified email, or null if not found
   */
  @Override
  public User findByEmail(String email) throws DaoLayerException {

    try {
      return userMapper.findByEmail(email);
    } catch (Exception e) {
      log.error("Error retrieving user with email {} for  {}", email, e.getMessage());
      throw new DaoLayerException("Failed to retrieve user by email", e);
    }
  }

  /**
   * Updates the password of a user based on the provided update and condition maps.
   *
   * @param updateMap a map containing the fields to be updated, including the new password
   * @param conditionMap a map containing conditions to identify which user(s) to update
   */
  @Override
  public void update(String tableName, Map<String, Object> updateMap, Map<String, Object> conditionMap)
      throws DaoLayerException {

    try {
      int rowCount = userMapper.update(tableName, updateMap, conditionMap);

      if (rowCount <= 0) {
        log.warn("No user was updated with conditions");
        throw new DaoLayerException("Failed to update user");
      }

    } catch (Exception e) {
      log.error("Error updating user with conditions {}: {}", conditionMap, e.getMessage());
      throw new DaoLayerException("Failed to update user", e);
    }
  }


  /**
   * Retrieves a list of all users associated with a specific account ID.
   *
   * @param accountId the unique identifier of the account
   * @return a list of users associated with the given account ID
   */
  @Override
  public List<User> listByAccountId(Long accountId) throws DaoLayerException {
    try {
      return userMapper.listByAccountId(accountId);
    } catch (Exception e) {
      log.error("Error retrieving users for account {}: {}", accountId, e.getMessage());
      throw new DaoLayerException("Failed to retrieve users for account", e);
    }
  }

  /**
   * Deletes all role associations for a specific user.
   *
   * @param userId the ID of the user whose role associations should be deleted
   * @param accountId the ID of the account to which the user belongs
   */
  @Override
  @Transactional(rollbackFor = DaoLayerException.class)
  public void deleteUserRoles(Long userId, Long accountId) throws DaoLayerException {
    try {
      int deletedRows = userMapper.deleteUserRoles(userId, accountId);
      log.debug("Deleted {} role associations for user {} in account {}", deletedRows, userId, accountId);
    } catch (Exception e) {
      log.error("Error deleting role associations for user {} in account {}: {}", userId, accountId, e.getMessage());
      throw new DaoLayerException("Failed to delete user role associations", e);
    }
  }

  /**
   * Assigns roles to a user by creating user-role associations.
   *
   * @param userId the ID of the user to assign roles to
   * @param roleIds the list of role IDs to assign to the user
   * @param accountId the ID of the account to which the user belongs
   */
  @Override
  @Transactional(rollbackFor = DaoLayerException.class)
  public void assignUserRoles(Long userId, List<Long> roleIds, Long accountId) throws DaoLayerException {
    if (roleIds == null || roleIds.isEmpty()) {
      log.debug("No roles to assign for user {} in account {}", userId, accountId);
      return;
    }

    try {
      for (Long roleId : roleIds) {
        userMapper.insertUserRole(userId, roleId, accountId);
      }
      log.debug("Assigned {} roles to user {} in account {}", roleIds.size(), userId, accountId);
    } catch (Exception e) {
      log.error("Error assigning roles {} to user {} in account {}: {}", roleIds, userId, accountId, e.getMessage());
      throw new DaoLayerException("Failed to assign user roles", e);
    }
  }
}
