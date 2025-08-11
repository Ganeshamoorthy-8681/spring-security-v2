package com.spring.security.dao;

import com.spring.security.dao.mapper.OtpMapper;
import com.spring.security.domain.entity.OtpCode;
import java.util.Map;

import com.spring.security.exceptions.DaoLayerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OtpDaoImpl implements OtpDao {

  private final OtpMapper otpMapper;

  /**
   * Constructor for OtpDaoImpl.
   *
   * @param otpMapper the OtpMapper to be used for database operations
   */
  public OtpDaoImpl(OtpMapper otpMapper) {
    this.otpMapper = otpMapper;
  }

  /**
   * Creates a new OTP in the database.
   *
   * @param otpCode the OTP code to be created
   */
  @Override
  public void create(OtpCode otpCode) throws DaoLayerException {
    try {
      int rowCount = otpMapper.create(otpCode);

      if(rowCount < 1){
        log.warn("Failed to create OTP for email: {}", otpCode.getEmail());
        throw new DaoLayerException("Failed to create OTP");
      }

    } catch (Exception e) {
      log.error("Error creating OTP for email: {}", otpCode.getEmail(), e);
      throw new DaoLayerException("Failed to create OTP",e);
    }
  }

  /**
   * Retrieves the latest OTP for a given email.
   *
   * @param email the email to retrieve the OTP for
   * @return the latest OTP code, or null if not found
   */
  @Override
  public OtpCode findByEmail(String email) throws DaoLayerException {

    try {
      return otpMapper.find(email);
    } catch (Exception e) {
      log.error("Error retrieving OTP for email: {}", email, e);
      throw new DaoLayerException("Failed to retrieve OTP",e);
    }
  }

  /**
   * Updates the OTP code associated with the specified email based on the provided updates and
   * conditions.
   *
   * @param updates A map containing the fields to be updated and their new values.
   * @param conditions A map containing the conditions that must be met for the update to occur.
   */
  @Override
  public void update(Map<String, Object> updates, Map<String, Object> conditions) throws DaoLayerException {

    try {
      int rowCount = otpMapper.update("otp_codes", updates, conditions);
      if (rowCount < 1){
        log.warn("No OTP was updated with conditions: {}", conditions);
        throw new DaoLayerException("Failed to update OTP");
      }
    } catch (Exception e) {
      log.error("Error updating OTP for email: {}", conditions.get("email"), e);
      throw new DaoLayerException("Failed to update OTP",e);
    }
  }

  /**
   * Deletes all OTPs for a given email.
   *
   * @param email the email to delete OTPs for
   */
  @Override
  public void deleteByEmail(String email) throws DaoLayerException {
    try {
      int rowCount = otpMapper.delete(email);
      if (rowCount < 1) {
        log.warn("No OTPs were deleted for email: {}", email);
        throw new DaoLayerException("Failed to delete OTP");
      }
    } catch (DaoLayerException e) {
      throw new DaoLayerException("Failed to delete OTP",e);
    }
  }
}
