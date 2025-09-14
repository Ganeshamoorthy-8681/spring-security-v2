package com.spring.security.service;

import com.spring.security.controller.dto.request.RootUserCreateRequestDto;
import com.spring.security.controller.dto.request.UserCreateRequestDto;
import com.spring.security.controller.dto.request.UserUpdateRequestDto;
import com.spring.security.controller.dto.response.UserCreateResponseDto;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.exceptions.ServiceLayerException;
import java.util.List;

/**
 * UserService interface for managing user-related operations. This interface defines methods for
 * creating and retrieving users.
 */
public interface UserService {

  /**
   * Creates a new user.
   *
   * @param userCreateRequestDto the DTO containing user creation details
   */
  UserCreateResponseDto createUser(UserCreateRequestDto userCreateRequestDto, Long accountId)
      throws ServiceLayerException;

  /**
   * Creates a root user with elevated privileges.
   *
   * @param userCreateRequestDto the DTO containing root user creation details
   */
  void createRootUser(RootUserCreateRequestDto userCreateRequestDto, Long accountId)
      throws ServiceLayerException;

  /**
   * Retrieves a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return the user with the specified ID, or null if not found
   */
  User findByAccountIdAndUserId(Long accountId, Long id) throws ServiceLayerException;

  /**
   * Retrieves a user by their email.
   *
   * @param email the email of the user to retrieve
   * @return the user with the specified email, or null if not found
   */
  User findByAccountIdAndEmail(Long accountId, String email) throws ServiceLayerException;

  /**
   * Updates the password for a user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email the email of the user whose password is to be updated
   * @param password the new password to set for the user
   */
  void updateUserPassword(Long accountId, String email, String password)
      throws ServiceLayerException;

  /**
   * Updates the status of a user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email the email of the user whose status is to be updated
   */
  void updateUserStatus(Long accountId, String email, UserStatus status)
      throws ServiceLayerException;

  /**
   * Sends a forgot password request for the user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email the email of the user who has forgotten their password
   */
  void forgotPassword(Long accountId, String email);

  /**
   * Updates the last login time for a user. Moves current login to last login and sets current
   * login to the current timestamp.
   *
   * @param accountId the ID of the account to which the user belongs (null for root users)
   * @param email the email of the user
   */
  void updateLastLoginTime(Long accountId, String email) throws ServiceLayerException;

  /**
   * Retrieves a list of all users associated with a specific account ID.
   *
   * @param accountId the unique identifier of the account
   * @return a list of users associated with the given account ID
   */
  List<User> listUsersByAccountId(Long accountId) throws ServiceLayerException;

  /**
   * Updates both profile information and roles for a user in a single operation. Note: Email
   * updates are not allowed for security reasons.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param userId the ID of the user to be updated
   * @param requestDto the DTO containing updated profile information and role assignments
   */
  void updateUser(Long accountId, Long userId, UserUpdateRequestDto requestDto)
      throws ServiceLayerException;
}
