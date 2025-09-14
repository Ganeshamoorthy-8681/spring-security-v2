package com.spring.security.service;

import static com.spring.security.domain.mapper.UserMapper.USER_MAPPER;

import com.spring.security.annotation.LogActivity;
import com.spring.security.controller.dto.request.RoleCreateRequestDto;
import com.spring.security.controller.dto.request.RootUserCreateRequestDto;
import com.spring.security.controller.dto.request.UserCreateRequestDto;
import com.spring.security.controller.dto.request.UserProfileUpdateRequestDto;
import com.spring.security.controller.dto.request.UserRoleUpdateRequestDto;
import com.spring.security.controller.dto.request.UserUpdateRequestDto;
import com.spring.security.controller.dto.response.UserCreateResponseDto;
import com.spring.security.dao.UserDao;
import com.spring.security.domain.entity.Role;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.entity.enums.UserType;
import com.spring.security.domain.mapper.UserMapper;
import com.spring.security.exceptions.DaoLayerException;
import com.spring.security.exceptions.ResourceAlreadyExistException;
import com.spring.security.exceptions.ResourceNotFoundException;
import com.spring.security.exceptions.ServiceLayerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing users. This service provides methods to create, retrieve,
 * update, and manage users.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  private final BCryptPasswordEncoder passwordEncoder;

  private final RoleService roleService;

  private final OtpService otpService;

  /**
   * Constructor for UserServiceImpl.
   *
   * @param userDao the UserDao to be used for database operations
   */
  public UserServiceImpl(
      UserDao userDao,
      BCryptPasswordEncoder passwordEncoder,
      RoleService roleService,
      OtpService otpService) {
    this.passwordEncoder = passwordEncoder;
    this.userDao = userDao;
    this.roleService = roleService;
    this.otpService = otpService;
  }

  /**
   * Creates a new user.
   *
   * @param requestDto the request dto containing user details
   */
  @Override
  @LogActivity(action = "CREATE", entityType = "USER", description = "New user created")
  public UserCreateResponseDto createUser(UserCreateRequestDto requestDto, Long accountId)
      throws ServiceLayerException {
    try {
      validateUserDoesNotExist(accountId, requestDto.getEmail());
      User user = buildUser(requestDto, accountId);
      User createdUser = persistUser(user);
      sendVerificationOtp(createdUser);
      return USER_MAPPER.convertUserToUserCreateResponseDto(createdUser);
    } catch (Exception e) {
      log.error("Error creating user: {}", e.getMessage(), e);
      throw new ServiceLayerException("Failed to create user", e);
    }
  }

  @Override
  @Transactional(rollbackFor = ServiceLayerException.class)
  @LogActivity(action = "CREATE", entityType = "USER", description = "Root user created")
  public void createRootUser(RootUserCreateRequestDto requestDto, Long accountId)
      throws ServiceLayerException {
    try {
      validateUserDoesNotExist(accountId, requestDto.getEmail());

      List<Role> roles = buildRootRoles(accountId);
      User user = buildRootUser(requestDto, accountId, roles);
      User createdUser = persistUser(user);
      sendVerificationOtp(createdUser);
    } catch (ResourceAlreadyExistException e) {
      log.error("Root user already exists: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("Error creating root user: {}", e.getMessage(), e);
      throw new ServiceLayerException("Failed to create root user", e);
    }
  }

  private void validateUserDoesNotExist(Long accountId, String email)
      throws ResourceAlreadyExistException {
    if (isUserAlreadyExists(accountId, email)) {
      throw new ResourceAlreadyExistException("User already exists");
    }
  }

  private User buildUser(UserCreateRequestDto dto, Long accountId) {
    return USER_MAPPER.convertUserCreateRequestDtoToUser(
        dto, UserType.PASSWORD, UserStatus.CREATED, accountId);
  }

  private User buildRootUser(RootUserCreateRequestDto dto, Long accountId, List<Role> roles) {
    return USER_MAPPER.convertRootUserCreateRequestDtoToUser(
        dto, UserType.PASSWORD, UserStatus.CREATED, accountId, roles);
  }

  private List<Role> buildRootRoles(Long accountId) throws ServiceLayerException {
    Role role = createRootRole(accountId);
    if (role == null) {
      throw new ServiceLayerException("Failed to create root role");
    }
    return List.of(role);
  }

  private User persistUser(User user) throws DaoLayerException {
    return userDao.create(user);
  }

  private void sendVerificationOtp(User user) throws ServiceLayerException {
    otpService.sendOtp(user.getEmail());
  }

  /**
   * Checks if a user with the specified account ID and email already exists.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email the email of the user to check
   * @return true if the user already exists, false otherwise
   */
  private boolean isUserAlreadyExists(Long accountId, String email) {
    try {
      findByAccountIdAndEmail(accountId, email);
      return true;
    } catch (ServiceLayerException e) {
      return false;
    }
  }

  /**
   * Creates a root role for the root user.
   *
   * @param accountId the ID of the account to which the root role belongs
   * @return the created root role response dto
   */
  private Role createRootRole(Long accountId) throws ServiceLayerException {
    RoleCreateRequestDto roleCreateRequestDto = new RoleCreateRequestDto();
    roleCreateRequestDto.setName("ROOT");
    roleCreateRequestDto.setDescription("Root role with all permissions");
    // Need to create a root role and assign it to the user. Only for root user creation
    return roleService.create(roleCreateRequestDto, accountId);
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return the user with the specified ID, or null if not found
   */
  @Override
  public User findByAccountIdAndUserId(Long accountId, Long id) throws ServiceLayerException {
    try {
      User user = userDao.findById(accountId, id);
      if (user == null) {
        log.warn("User with ID {} not found in account {}", id, accountId);
        throw new ResourceNotFoundException("User not found");
      }
      return user;
    } catch (DaoLayerException e) {
      log.error("Failed to find user by ID {}: {}", id, e.getMessage());
      throw new ServiceLayerException("Failed to find user by ID");
    }
  }

  /**
   * Retrieves a user by their email.
   *
   * @param email the email of the user to retrieve
   * @return the user with the specified email, or null if not found
   */
  @Override
  public User findByAccountIdAndEmail(Long accountId, String email) throws ServiceLayerException {
    try {
      User user = userDao.findByAccountIdAndEmail(accountId, email);
      if (user == null) {
        log.warn("User with email {} not found in account {}", email, accountId);
        throw new ResourceNotFoundException("User not found");
      }
      return user;
    } catch (DaoLayerException e) {
      log.error("Failed to find user by email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to find user by email");
    }
  }

  /**
   * Updates the password for a user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email the email of the user whose password is to be updated
   * @param password the new password to set for the user
   */
  @Override
  @LogActivity(action = "UPDATE", entityType = "USER", description = "User password updated")
  public void updateUserPassword(Long accountId, String email, String password)
      throws ServiceLayerException {

    try {
      Map<String, Object> updateMap = Map.of("password", passwordEncoder.encode(password));
      Map<String, Object> conditionMap = Map.of("email", email, "account_id", accountId);
      userDao.update("users",updateMap, conditionMap);

    } catch (DaoLayerException e) {
      log.error("Failed to update user password for email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to update user password");
    }
  }

  /**
   * Updates the status of a user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email the email of the user whose status is to be updated
   */
  @Override
  @LogActivity(action = "UPDATE", entityType = "USER", description = "User status updated")
  public void updateUserStatus(Long accountId, String email, UserStatus status)
      throws ServiceLayerException {
    try {
      Map<String, Object> updateMap = Map.of("status", status);
      Map<String, Object> conditionMap = Map.of("email", email, "account_id", accountId);
      userDao.update("users", updateMap, conditionMap);
    } catch (DaoLayerException e) {
      log.error("Failed to update user status for email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to update user status");
    }
  }

  /**
   * Sends a forgot password request for the user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email the email of the user who has forgotten their password
   */
  @Override
  public void forgotPassword(Long accountId, String email) {
    // Need to implement forgot password functionality
  }

  /**
   * Updates the last login time for a user. Moves current login to last login
   * and sets current login to the current timestamp.
   *
   * @param accountId the ID of the account to which the user belongs (null for root users)
   * @param email the email of the user
   */
  @Override
  @LogActivity(action = "UPDATE", entityType = "USER", description = "User login time updated")
  public void updateLastLoginTime(Long accountId, String email) throws ServiceLayerException {
    try {
      // Create update map with login time fields
      Map<String, Object> updateMap = Map.of(
          "last_login", "$col:current_login",  // Move current_login to last_login $col: for direct column reference
          "current_login", "CURRENT_TIMESTAMP"  // Set current_login to now
      );

      Map<String, Object> conditionMap = new HashMap<>();
      conditionMap.put("email", email);

      if (accountId != null) {
        conditionMap.put("account_id",accountId);
      }

      userDao.update("users",updateMap, conditionMap);
      log.debug("Successfully updated login time for user: {} on account: {}", email, accountId);
    } catch (DaoLayerException e) {
      log.error("Failed to update login time for user {} on account {}: {}", email, accountId, e.getMessage());
      throw new ServiceLayerException("Failed to update user login time", e);
    }
  }

  /**
   * Retrieves a list of all users associated with a specific account ID.
   *
   * @param accountId the unique identifier of the account
   * @return a list of users associated with the given account ID
   */
  @Override
  public List<User> listUsersByAccountId(Long accountId) throws ServiceLayerException {
    try {
      List<User> users = userDao.listByAccountId(accountId);
      log.debug("Retrieved {} users for account ID: {}", users.size(), accountId);
      return users;
    } catch (DaoLayerException e) {
      log.error("Failed to retrieve users for account ID {}: {}", accountId, e.getMessage());
      throw new ServiceLayerException("Failed to retrieve users", e);
    }
  }

  /**
   * Updates the profile information for a user identified by their account ID and user ID.
   * Note: Email updates are not allowed for security reasons.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param userId the ID of the user whose profile is to be updated
   * @param requestDto the DTO containing updated profile information
   */
  @Override
  @LogActivity(action = "UPDATE", entityType = "USER", description = "User profile updated")
  public void updateUserProfile(Long accountId, Long userId, UserProfileUpdateRequestDto requestDto)
      throws ServiceLayerException {
    try {
      // First verify the user exists
      findByAccountIdAndUserId(accountId, userId);

      Map<String, Object> updateMap = new HashMap<>();
      updateMap.put("first_name", requestDto.getFirstName());
      updateMap.put("last_name", requestDto.getLastName());

      if (requestDto.getMiddleName() != null) {
        updateMap.put("middle_name", requestDto.getMiddleName());
      }

      Map<String, Object> conditionMap = Map.of("id", userId, "account_id", accountId);
      userDao.update("users",updateMap, conditionMap);

      log.info("Successfully updated profile for user ID: {} in account: {}", userId, accountId);
    } catch (DaoLayerException e) {
      log.error("Failed to update user profile for user ID {}: {}", userId, e.getMessage());
      throw new ServiceLayerException("Failed to update user profile", e);
    }
  }

  /**
   * Updates the roles assigned to a user identified by their account ID and user ID.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param userId the ID of the user whose roles are to be updated
   * @param requestDto the DTO containing the new role assignments
   */
  @Override
  @Transactional(rollbackFor = ServiceLayerException.class)
  @LogActivity(action = "UPDATE", entityType = "USER", description = "User roles updated")
  public void updateUserRoles(Long accountId, Long userId, UserRoleUpdateRequestDto requestDto)
      throws ServiceLayerException {
    try {
      // First verify the user exists
      findByAccountIdAndUserId(accountId, userId);

      // Validate that all role IDs exist and belong to the same account
      List<Role> validRoles = new ArrayList<>();
      for (Long roleId : requestDto.getRoleIds()) {
        Role role = roleService.findById(roleId, accountId);
        if (role == null) {
          throw new ResourceNotFoundException("Role with ID " + roleId + " not found");
        }
        validRoles.add(role);
      }

      // Delete existing role associations first
      userDao.deleteUserRoles(userId, accountId);

      // Assign new roles
      List<Long> roleIds = requestDto.getRoleIds();
      userDao.assignUserRoles(userId, roleIds, accountId);

      log.info("Successfully updated roles for user ID: {} in account: {}", userId, accountId);
    } catch (DaoLayerException e) {
      log.error("Failed to update user roles for user ID {}: {}", userId, e.getMessage());
      throw new ServiceLayerException("Failed to update user roles", e);
    }
  }

  /**
   * Updates both profile information and roles for a user in a single operation.
   * Note: Email updates are not allowed for security reasons.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param userId the ID of the user to be updated
   * @param requestDto the DTO containing updated profile information and role assignments
   */
  @Override
  @Transactional(rollbackFor = ServiceLayerException.class)
  @LogActivity(action = "UPDATE", entityType = "USER", description = "User profile and roles updated")
  public void updateUser(Long accountId, Long userId, UserUpdateRequestDto requestDto)
      throws ServiceLayerException {
    try {
      // First verify the user exists
      findByAccountIdAndUserId(accountId, userId);

      // Prepare profile update
      Map<String, Object> updateMap = new HashMap<>();
      updateMap.put("first_name", requestDto.getFirstName());
      updateMap.put("last_name", requestDto.getLastName());

      if (requestDto.getMiddleName() != null) {
        updateMap.put("middle_name", requestDto.getMiddleName());
      }

      updateMap.put("updated_at", "CURRENT_TIMESTAMP");

      // Update profile information
      Map<String, Object> conditionMap = Map.of("id", userId, "account_id", accountId);
      userDao.update("users", updateMap, conditionMap);

      // Update roles if provided
      if (requestDto.getRoleIds() != null && !requestDto.getRoleIds().isEmpty()) {

        // Validate that all role IDs exist and belong to the same account
        List<Role> validRoles = new ArrayList<>();
        for (Long roleId : requestDto.getRoleIds()) {
          Role role = roleService.findById(roleId, accountId);
          if (role == null) {
            throw new ResourceNotFoundException("Role with ID " + roleId + " not found");
          }
          validRoles.add(role);
        }

        // Delete existing role associations first
        userDao.deleteUserRoles(userId, accountId);

        // Assign new roles
        userDao.assignUserRoles(userId, requestDto.getRoleIds(), accountId);
      }

      log.info("Successfully updated user profile and roles for user ID: {} in account: {}", userId, accountId);
    } catch (DaoLayerException e) {
      log.error("Failed to update user for user ID {}: {}", userId, e.getMessage());
      throw new ServiceLayerException("Failed to update user", e);
    }
  }



}
