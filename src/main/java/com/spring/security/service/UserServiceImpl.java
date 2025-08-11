package com.spring.security.service;

import static com.spring.security.domain.mapper.UserMapper.USER_MAPPER;

import com.spring.security.component.OtpGenerator;
import com.spring.security.controller.dto.request.RoleCreateRequestDto;
import com.spring.security.controller.dto.request.RootUserCreateRequestDto;
import com.spring.security.controller.dto.request.UserCreateRequestDto;
import com.spring.security.controller.dto.request.OtpValidateRequestDto;
import com.spring.security.controller.dto.response.OtpValidationStatus;
import com.spring.security.controller.dto.response.RoleResponseDto;
import com.spring.security.controller.dto.response.UserCreateResponseDto;
import com.spring.security.controller.dto.response.UserResponseDto;
import com.spring.security.controller.dto.response.OtpValidateResponseDto;
import com.spring.security.dao.UserDao;
import com.spring.security.domain.entity.OtpCode;
import com.spring.security.domain.entity.Role;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.entity.enums.UserType;
import com.spring.security.domain.mapper.RoleMapper;
import com.spring.security.domain.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.spring.security.exceptions.DaoLayerException;
import com.spring.security.exceptions.EmailServiceException;
import com.spring.security.exceptions.OtpGenerationFailedException;
import com.spring.security.exceptions.ResourceAlreadyExistException;
import com.spring.security.exceptions.ResourceNotFoundException;
import com.spring.security.exceptions.ServiceLayerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  private final BCryptPasswordEncoder passwordEncoder;

  private final OtpService otpService;

  private final EmailService emailService;

  private final RoleService roleService;

  private final OtpGenerator otpGenerator;

  private final AccountService accountService;

  /**
   * Constructor for UserServiceImpl.
   *
   * @param userDao the UserDao to be used for database operations
   */
  UserServiceImpl(
      UserDao userDao,
      BCryptPasswordEncoder passwordEncoder,
      OtpService otpService,
      EmailService emailService,
      RoleService roleService,
      OtpGenerator otpGenerator,
      AccountService accountService) {
    this.passwordEncoder = passwordEncoder;
    this.userDao = userDao;
    this.otpService = otpService;
    this.emailService = emailService;
    this.roleService = roleService;
    this.otpGenerator = otpGenerator;
    this.accountService = accountService;
  }

  /**
   * Creates a new user.
   *
   * @param requestDto the request dto containing user details
   */
  @Override
  public UserCreateResponseDto createUser(UserCreateRequestDto requestDto, Long accountId) throws ServiceLayerException {

    try {
      User user =
              USER_MAPPER.convertUserCreateRequestDtoToUser(
                      requestDto, UserType.PASSWORD, UserStatus.CREATED, accountId);

        // Check if user already exists
        if (isUserAlreadyExists(accountId, user.getEmail())) {
            log.error("User with email {} already exists in account {}", user.getEmail(), accountId);
            throw new ResourceAlreadyExistException("User already exists");
        }

      User createdUser = userDao.create(user);
      // Sends an email for verification
      sendOtpToEmail(createdUser.getEmail());

      return UserMapper.USER_MAPPER.convertUserToUserCreateResponseDto(createdUser);
    } catch (DaoLayerException e) {
      log.error("Failed to create user: {}", e.getMessage());
      throw new ServiceLayerException("Failed to create user");
    }
    catch (Exception e) {
      log.error("Unexpected error occurred while creating user: {}", e.getMessage());
      throw new ServiceLayerException("Unexpected error occurred while creating user");
    }
  }

  /**
   * Creates a root user with the specified details.
   *
   * @param userCreateRequestDto the request dto containing root user creation details
   * @param accountId the ID of the account to which the root user belongs
   * @return the created root user response dto
   */
  @Override
  @Transactional(rollbackFor = ServiceLayerException.class)
  public UserResponseDto createRootUser(
      RootUserCreateRequestDto userCreateRequestDto, Long accountId) throws ServiceLayerException {

    try {

      if(isUserAlreadyExists(accountId, userCreateRequestDto.getEmail())) {
        log.error("Root user with email {} already exists in account {}", userCreateRequestDto.getEmail(), accountId);
        throw new ResourceAlreadyExistException("Root user already exists");
      }

      RoleResponseDto roleResponseDto =  createRootRole(accountId);

      if (roleResponseDto == null) {
        log.error("Failed to create root role for user creation");
        throw new ServiceLayerException("Failed to create roles for root user");
      }
      List<Role> roles =
              List.of(RoleMapper.ROLE_MAPPER.convertRoleResponseDtoToRole(roleResponseDto));
      User user =
              USER_MAPPER.convertRootUserCreateRequestDtoToUser(
                      userCreateRequestDto, UserType.PASSWORD, UserStatus.CREATED, accountId, roles);
      User createdUser = userDao.create(user);
      // Sends an email for verification
      sendOtpToEmail(createdUser.getEmail());
      return UserMapper.USER_MAPPER.convertUserToUserResponseDto(createdUser);

    } catch (DaoLayerException | ServiceLayerException e) {
      log.error("Failed to create root user: {}", e.getMessage());
      throw new ServiceLayerException("Failed to create root user");

    }  catch (Exception e) {
      log.error("Unexpected error occurred while creating Root  user: {}", e.getMessage());
      throw new ServiceLayerException("Unexpected error occurred while creating user");
    }
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
  private RoleResponseDto createRootRole(Long accountId) throws ServiceLayerException {
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
  public UserResponseDto findByAccountIdAndUserId(Long accountId, Long id) throws ServiceLayerException {
    try {

      User user = userDao.findById(accountId, id);

      if( user == null) {
        log.warn("User with ID {} not found in account {}", id, accountId);
        throw new ResourceNotFoundException("User not found");
      }
      return USER_MAPPER.convertUserToUserResponseDto(user);
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
  public UserResponseDto findByAccountIdAndEmail(Long accountId, String email) throws ServiceLayerException {
    try{
      User user = userDao.findByAccountIdAndEmail(accountId, email);
      if(user == null) {
        log.warn("User with email {} not found in account {}", email, accountId);
        throw new ResourceNotFoundException("User not found");
      }
      return USER_MAPPER.convertUserToUserResponseDto(user);
    } catch (DaoLayerException e) {
      log.error("Failed to find user by email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to find user by email");
    }
  }

  /**
   * Verifies the email address of an account.
   *
   * @param email the email address to verify
   */
  private void sendOtpToEmail(String email) throws ServiceLayerException {
    try {
      String otp = otpGenerator.generateOtp();
      otpService.create(email, otp);
      sendEmail(email, otp);
    } catch (OtpGenerationFailedException | EmailServiceException e){
        log.error("Failed to generate OTP for email {}: {}", email, e.getMessage());
        throw new ServiceLayerException("Failed to generate OTP",e);
        }
  }

  /**
   * Sends an email with the OTP code to the specified email address.
   *
   * @param email the email address to which the OTP should be sent
   * @param otp the OTP code to send
   */
  private void sendEmail(String email, String otp) throws EmailServiceException {
    emailService.sendEmail(email, "Email Verification", "Your OTP code is: " + otp);
    log.info("Email sent to {} with OTP: {}", email, otp);
  }

  /**
   * Verifies the OTP code for an account.
   *
   * @param otpRequestDto the request dto for verifying the OTP
   */
  @Override
  @Transactional(rollbackFor = ServiceLayerException.class)
  public OtpValidateResponseDto validateOtp(OtpValidateRequestDto otpRequestDto) throws ServiceLayerException {

    try {
      OtpCode otpCode = otpService.find(otpRequestDto.getEmail());
      return processOtpValidation(otpRequestDto, otpCode);

    } catch (ServiceLayerException e) {
      log.error("Failed to validate OTP: {}", e.getMessage());
      throw new ServiceLayerException("Failed to validate OTP");
    }
  }

    /**
     * Processes the OTP validation request.
     *
     * @param request the OTP validation request containing email and OTP
     * @param otpCode the OTP code associated with the email
     * @return the response indicating the status of the OTP validation
     */
  private OtpValidateResponseDto processOtpValidation(OtpValidateRequestDto request, OtpCode otpCode) throws ServiceLayerException {
    OtpValidateResponseDto response = new OtpValidateResponseDto();

    if (otpCode == null) {
      log.warn("No OTP found for email");
      response.setStatus(OtpValidationStatus.NOT_FOUND);
      return response;
    }

    if (otpCode.getExpiresAt().isBefore(LocalDateTime.now())) {
      log.warn("OTP has expired");
      response.setStatus(OtpValidationStatus.EXPIRED);
      return response;
    }

    if (!otpCode.getOtp().equals(String.valueOf(request.getOtp()))) {
      response.setStatus(OtpValidationStatus.INVALID);
      return response;
    }

    activateUserIfRequired(request);
    response.setStatus(OtpValidationStatus.VALID);
    return response;
  }

    /**
     * Activates the user if they are a root user and updates their status to active.
     * @param request the OTP validation request containing account ID and email
     *
     */
  private void activateUserIfRequired(OtpValidateRequestDto request) throws ServiceLayerException {

    try {
       UserResponseDto user = findByAccountIdAndEmail(request.getAccountId(), request.getEmail());

      if (isRootUser(user)) {
        accountService.updateStatus(request.getAccountId(), AccountStatus.ACTIVE);
      }

      updateUserStatus(request.getAccountId(), user.getEmail(), UserStatus.ACTIVE);
    } catch (ServiceLayerException e) {
      log.error("Failed to activate user: {}", e.getMessage());
      throw new ServiceLayerException("Failed to activate user");
    }

  }

  /**
   * Resends the OTP (One-Time Password) to the specified email.
   *
   * @param email the email to which the OTP should be resent
   */
  @Override
  public void resendOtp(String email) throws ServiceLayerException {
    sendOtpToEmail(email);
  }

  /**
   * Updates the password for a user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email the email of the user whose password is to be updated
   * @param password the new password to set for the user
   */
  @Override
  public void updateUserPassword(Long accountId, String email, String password) throws ServiceLayerException {

    try {
      Map<String, Object> updateMap = Map.of("password", passwordEncoder.encode(password));
      Map<String, Object> conditionMap = Map.of("email", email, "account_id", accountId);
      userDao.update(updateMap, conditionMap);

    } catch (DaoLayerException e) {
      log.error("Failed to update user password for email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to update user password");
    }
  }

  /**
   * Updates the status of a user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email     the email of the user whose status is to be updated
   */
  @Override
  public void updateUserStatus(Long accountId, String email, UserStatus status) throws ServiceLayerException {

    try {
      Map<String, Object> updateMap = Map.of("status", status);
      Map<String, Object> conditionMap = Map.of("email", email, "account_id", accountId);
      userDao.update(updateMap, conditionMap);
    } catch (DaoLayerException e) {
      log.error("Failed to update user status for email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to update user status");
    }
  }


  /**
   * Sends a forgot password request for the user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email     the email of the user who has forgotten their password
   */
  @Override
  public void forgotPassword(Long accountId, String email) {
    // Need to implement forgot password functionality
  }

    /**
     * Checks if the user is a root user.
     *
     * @param user the user to check
     * @return true if the user is a root user, false otherwise
     */
  private boolean isRootUser(UserResponseDto user) {
    return user.getRoles().stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase("ROOT"));
  }

}
