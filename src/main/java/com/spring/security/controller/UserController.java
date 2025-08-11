package com.spring.security.controller;

import com.spring.security.controller.dto.request.ForgotPasswordRequestDto;
import com.spring.security.controller.dto.request.GetUserByEmailRequestDto;
import com.spring.security.controller.dto.request.RootUserCreateRequestDto;
import com.spring.security.controller.dto.request.SetUserPasswordRequestDto;
import com.spring.security.controller.dto.request.UserCreateRequestDto;
import com.spring.security.controller.dto.response.UserCreateResponseDto;
import com.spring.security.controller.dto.response.UserResponseDto;
import com.spring.security.exceptions.ServiceLayerException;
import com.spring.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
      @PathVariable Long accountId, @RequestBody UserCreateRequestDto requestDto) throws ServiceLayerException {
    return new ResponseEntity<>(userService.createUser(requestDto, accountId), HttpStatus.CREATED);
  }

  /**
   * Creates a root user.
   *
   * @param requestDto the request data transfer object containing root user creation details
   * @return a ResponseEntity indicating the result of the operation
   */
  @PostMapping("/root/create")
  public ResponseEntity<UserResponseDto> createRootUser(
      @PathVariable Long accountId, @RequestBody RootUserCreateRequestDto requestDto) throws ServiceLayerException {
    return new ResponseEntity<>(
        userService.createRootUser(requestDto, accountId), HttpStatus.CREATED);
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
    UserResponseDto user = userService.findByAccountIdAndUserId(accountId, userId);
    return new ResponseEntity<>(user, HttpStatus.OK);
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
      @PathVariable Long accountId, @RequestBody GetUserByEmailRequestDto requestDto) throws ServiceLayerException {
    UserResponseDto userResponseDto = userService.findByAccountIdAndEmail(accountId, requestDto.getEmail());
    return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
  }


  /**
   * Sets a new password for a user.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param requestDto the request data transfer object containing the email and new password
   * @return a ResponseEntity indicating the result of the operation
   */
  @PostMapping("/set-password")
  public ResponseEntity<Void> setPassword(
      @PathVariable Long accountId, @RequestBody SetUserPasswordRequestDto requestDto) throws ServiceLayerException {
    //Need to validate the user state and existence before setting the password
    userService.updateUserPassword(accountId, requestDto.getEmail(), requestDto.getPassword());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
        @PathVariable Long accountId, @RequestBody ForgotPasswordRequestDto requestDto) {
        userService.forgotPassword(accountId, requestDto.getEmail());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
