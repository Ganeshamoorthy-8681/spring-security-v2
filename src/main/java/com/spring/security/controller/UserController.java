package com.spring.security.controller;

import com.spring.security.controller.dto.request.ForgotPasswordRequestDto;
import com.spring.security.controller.dto.request.GetUserByEmailRequestDto;
import com.spring.security.controller.dto.request.SetUserPasswordRequestDto;
import com.spring.security.controller.dto.request.UserCreateRequestDto;
import com.spring.security.controller.dto.request.UserUpdateRequestDto;
import com.spring.security.controller.dto.response.UserCreateResponseDto;
import com.spring.security.controller.dto.response.UserResponseDto;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.mapper.UserMapper;
import com.spring.security.exceptions.ServiceLayerException;
import com.spring.security.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** User controller for handling user-related operations. */
@RestController
@RequestMapping("/api/v1/accounts/{accountId}/users")
public class UserController {

  private final UserService userService;

  /**
   * Constructor for UserController.
   *
   * @param userService the service to handle user-related operations
   */
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Creates a new IAM user.
   *
   * @param requestDto the request data transfer object containing user creation details
   * @return a ResponseEntity indicating the result of the operation
   */
  @PostMapping("/create")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:USER:CREATE')")
  public ResponseEntity<UserCreateResponseDto> createUser(
      @PathVariable Long accountId, @RequestBody UserCreateRequestDto requestDto)
      throws ServiceLayerException {
    return new ResponseEntity<>(userService.createUser(requestDto, accountId), HttpStatus.CREATED);
  }

  /**
   * Sets a new password for a user.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param requestDto the request data transfer object containing the email and new password
   * @return a ResponseEntity indicating the result of the operation
   */
  @PatchMapping("/set-password")
  public ResponseEntity<Void> setPassword(
      @PathVariable Long accountId, @RequestBody SetUserPasswordRequestDto requestDto)
      throws ServiceLayerException {
    // Need to validate the user state and existence before setting the password
    userService.updateUserPassword(accountId, requestDto.getEmail(), requestDto.getPassword());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/forgot-password")
  public ResponseEntity<Void> forgotPassword(
      @PathVariable Long accountId, @RequestBody ForgotPasswordRequestDto requestDto) {
    userService.forgotPassword(accountId, requestDto.getEmail());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param userId the ID of the user to retrieve
   * @return a ResponseEntity containing the user details
   */
  @GetMapping("/{userId}")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:USER:READ')")
  public ResponseEntity<UserResponseDto> getUserById(
      @PathVariable Long accountId, @PathVariable Long userId) throws ServiceLayerException {
    User user = userService.findByAccountIdAndUserId(accountId, userId);
    return new ResponseEntity<>(
        UserMapper.USER_MAPPER.convertUserToUserResponseDto(user), HttpStatus.OK);
  }

  /**
   * Retrieves a user by their email.
   *
   * @param requestDto the request data transfer object containing the email to search for
   * @return a ResponseEntity containing the user details
   */
  @PostMapping("/email")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:USER:READ')")
  public ResponseEntity<UserResponseDto> getUserByEmail(
      @PathVariable Long accountId, @RequestBody GetUserByEmailRequestDto requestDto)
      throws ServiceLayerException {
    User user = userService.findByAccountIdAndEmail(accountId, requestDto.getEmail());
    return new ResponseEntity<>(
        UserMapper.USER_MAPPER.convertUserToUserResponseDto(user), HttpStatus.OK);
  }

  /**
   * Retrieves a list of all users in an account.
   *
   * @param accountId the ID of the account to retrieve users from
   * @return a ResponseEntity containing the list of users
   */
  @GetMapping("/list")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:USER:LIST')")
  public ResponseEntity<List<UserResponseDto>> listUsers(@PathVariable Long accountId)
      throws ServiceLayerException {
    List<User> users = userService.listUsersByAccountId(accountId);
    List<UserResponseDto> userResponseDtos =
        users.stream().map(UserMapper.USER_MAPPER::convertUserToUserResponseDto).toList();
    return new ResponseEntity<>(userResponseDtos, HttpStatus.OK);
  }

  /**
   * Updates both profile information and roles for a user in a single operation. Note: Email
   * updates are not allowed for security reasons. Roles are optional - if not provided, only
   * profile will be updated.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param userId the ID of the user to be updated
   * @param requestDto the DTO containing updated profile information and optional role assignments
   * @return a ResponseEntity indicating the result of the operation
   */
  @PatchMapping("/{userId}")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:USER:UPDATE')")
  public ResponseEntity<Void> updateUser(
      @PathVariable Long accountId,
      @PathVariable Long userId,
      @Valid @RequestBody UserUpdateRequestDto requestDto)
      throws ServiceLayerException {
    userService.updateUser(accountId, userId, requestDto);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Enables a user by updating their status to ACTIVE.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param userId the ID of the user to be enabled
   * @return a ResponseEntity indicating the result of the operation
   */
  @PatchMapping("/{userId}/enable")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:USER:UPDATE')")
  public ResponseEntity<Void> updateUserStatus(
      @PathVariable Long accountId, @PathVariable Long userId) throws ServiceLayerException {
    userService.updateUserStatusById(accountId, userId, UserStatus.ACTIVE);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Disables a user by updating their status to INACTIVE.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param userId the ID of the user to be disabled
   * @return a ResponseEntity indicating the result of the operation
   */
  @PatchMapping("/{userId}/disable")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:USER:UPDATE')")
  public ResponseEntity<Void> disableUser(@PathVariable Long accountId, @PathVariable Long userId)
      throws ServiceLayerException {
    userService.updateUserStatusById(accountId, userId, UserStatus.INACTIVE);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
