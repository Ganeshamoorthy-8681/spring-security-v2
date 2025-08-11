package com.spring.security.service;

import com.spring.security.controller.dto.request.RootUserCreateRequestDto;
import com.spring.security.controller.dto.request.UserCreateRequestDto;
import com.spring.security.controller.dto.request.OtpValidateRequestDto;
import com.spring.security.controller.dto.response.UserCreateResponseDto;
import com.spring.security.controller.dto.response.UserResponseDto;
import com.spring.security.controller.dto.response.OtpValidateResponseDto;
import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.exceptions.ServiceLayerException;

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
  UserCreateResponseDto createUser(UserCreateRequestDto userCreateRequestDto, Long accountId) throws ServiceLayerException;

  /**
   * Creates a root user with elevated privileges.
   *
   * @param userCreateRequestDto the DTO containing root user creation details
   * @return the created root user response DTO
   */
  UserResponseDto createRootUser(RootUserCreateRequestDto userCreateRequestDto, Long accountId) throws ServiceLayerException;

  /**
   * Retrieves a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return the user with the specified ID, or null if not found
   */
  UserResponseDto findByAccountIdAndUserId(Long accountId, Long id) throws ServiceLayerException;

  /**
   * Retrieves a user by their email.
   *
   * @param email the email of the user to retrieve
   * @return the user with the specified email, or null if not found
   */
  UserResponseDto findByAccountIdAndEmail(Long accountId, String email) throws ServiceLayerException;

  /**
   * Validates the provided OTP (One-Time Password) for the given email.
   *
   * @param otpValidateRequestDto the DTO containing the email and OTP to validate
   */
  OtpValidateResponseDto validateOtp(OtpValidateRequestDto otpValidateRequestDto) throws ServiceLayerException;

  /**
   * Resends the OTP (One-Time Password) to the specified email.
   *
   * @param email the email to which the OTP should be resent
   */
  void resendOtp(String email) throws ServiceLayerException;

  /**
   * Updates the password for a user identified by their account ID and email.
   *
   * @param accountId the ID of the account to which the user belongs
   * @param email the email of the user whose password is to be updated
   * @param password the new password to set for the user
   */
  void updateUserPassword(Long accountId, String email, String password) throws ServiceLayerException;


    /**
     * Updates the status of a user identified by their account ID and email.
     *
     * @param accountId the ID of the account to which the user belongs
     * @param email the email of the user whose status is to be updated
     */
    void updateUserStatus(Long accountId, String email, UserStatus status) throws ServiceLayerException;

    /**
     * Sends a forgot password request for the user identified by their account ID and email.
     *
     * @param accountId the ID of the account to which the user belongs
     * @param email the email of the user who has forgotten their password
     */
    void forgotPassword(Long accountId, String email);
}
