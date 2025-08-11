package com.spring.security.service;

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
}
