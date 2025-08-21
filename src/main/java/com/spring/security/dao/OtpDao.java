package com.spring.security.dao;

import com.spring.security.domain.entity.OtpCode;
import com.spring.security.exceptions.DaoLayerException;
import java.util.Map;

/**
 * Data Access Object (DAO) interface for handling operations related to OTP (One-Time Password)
 * codes. This interface defines methods for creating, finding, and deleting OTP codes associated
 * with an email.
 */
public interface OtpDao {

  /**
   * Creates a new OTP code for the specified email.
   *
   * @param otpCode The OTP code to be created, containing the email and the code.
   */
  void create(OtpCode otpCode) throws DaoLayerException;

  /**
   * Finds the OTP code associated with the specified email.
   *
   * @param email The email for which to find the OTP code.
   * @return The OTP code associated with the email, or null if no code exists.
   */
  OtpCode findByEmail(String email) throws DaoLayerException;

  /**
   * Updates the OTP code associated with the specified email based on the provided updates and
   * conditions.
   *
   * @param updates A map containing the fields to be updated and their new values.
   * @param conditions A map containing the conditions that must be met for the update to occur.
   */
  void update(Map<String, Object> updates, Map<String, Object> conditions) throws DaoLayerException;

  /**
   * Deletes the OTP code associated with the specified email.
   *
   * @param email The email for which to delete the OTP code.
   */
  void deleteByEmail(String email) throws DaoLayerException;
}
