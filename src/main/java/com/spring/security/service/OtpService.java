package com.spring.security.service;

import com.spring.security.controller.dto.request.OtpValidateRequestDto;
import com.spring.security.controller.dto.response.OtpValidateResponseDto;
import com.spring.security.domain.entity.OtpCode;
import com.spring.security.exceptions.ServiceLayerException;

/** Service interface for managing One-Time Password (OTP) codes. */
public interface OtpService {

  /**
   * Creates a new OTP for the specified email address.
   *
   * @param email the email address to associate with the OTP
   */
  void create(String email, String otp) throws ServiceLayerException;

  /**
   * Finds the OTP associated with the specified email address.
   *
   * @param email the email address to search for
   * @return the OtpCode associated with the email, or null if not found
   */
  OtpCode find(String email) throws ServiceLayerException;

  /**
   * Updates the OTP for the specified email address.
   *
   * @param email the email address whose OTP should be updated
   */
  void update(String email, String otp) throws ServiceLayerException;

  /**
   * Deletes the OTP associated with the specified email address.
   *
   * @param email the email address whose OTP should be deleted
   */
  void delete(String email) throws ServiceLayerException;

  /**
   * Validates the provided OTP (One-Time Password) for the given email.
   *
   * @param otpValidateRequestDto the DTO containing the email and OTP to validate
   */
  OtpValidateResponseDto validateOtp(OtpValidateRequestDto otpValidateRequestDto)
      throws ServiceLayerException;

  /**
   * Resends the OTP (One-Time Password) to the specified email.
   *
   * @param email the email to which the OTP should be resent
   */
  void resendOtp(String email) throws ServiceLayerException;

  /**
   * Sends an OTP (One-Time Password) to the specified email.
   *
   * @param email the email to which the OTP should be sent
   */
  void sendOtp(String email) throws ServiceLayerException;
}
