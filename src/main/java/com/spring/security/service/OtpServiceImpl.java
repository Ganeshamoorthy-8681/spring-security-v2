package com.spring.security.service;

import com.spring.security.dao.OtpDao;
import com.spring.security.domain.entity.OtpCode;
import com.spring.security.exceptions.DaoLayerException;
import com.spring.security.exceptions.ResourceNotFoundException;
import com.spring.security.exceptions.ServiceLayerException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling OTP (One-Time Password) operations. This service provides
 * methods to create, find, update, delete, and validate OTPs.
 */
@Service
@Slf4j
public class OtpServiceImpl implements OtpService {

  private final OtpDao otpDao;

  /** Default constructor for OtpServiceImpl. */
  public OtpServiceImpl(OtpDao otpDao) {
    this.otpDao = otpDao;
  }

  /**
   * Creates a new OTP for the specified email address.
   *
   * @param email the email address to associate with the OTP
   */
  @Override
  public void create(String email, String otp) throws ServiceLayerException {

    try {
      OtpCode otpCode = new OtpCode();
      otpCode.setEmail(email);
      otpCode.setOtp(otp);
      otpCode.setCreatedAt(LocalDateTime.now());
      otpCode.setExpiresAt(LocalDateTime.now().plusMinutes(3)); // OTP valid for 3 minutes
      otpDao.create(otpCode);
    } catch (Exception e) {
      log.error("Error creating OTP for email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to create OTP");
    }
  }

  /**
   * Finds the OTP associated with the specified email address.
   *
   * @param email the email address to search for
   * @return the OtpCode associated with the email, or null if not found
   */
  @Override
  public OtpCode find(String email) throws ServiceLayerException {
    try {
      OtpCode otpCode = otpDao.findByEmail(email);
      if (otpCode == null) {
        throw new ResourceNotFoundException("OTP not found for email: " + email);
      }
      return otpCode;

    } catch (DaoLayerException e) {
      throw new ServiceLayerException("Failed to find OTP for email: " + email);
    }
  }

  /**
   * Updates the OTP for the specified email address.
   *
   * @param email the email address whose OTP should be updated
   */
  @Override
  public void update(String email, String otp) throws ServiceLayerException {
    try {
      Map<String, Object> updates =
          Map.of("otp", otp, "expires_at", LocalDateTime.now().plusMinutes(3));
      Map<String, Object> conditions = Map.of("email", email);
      otpDao.update(updates, conditions);
    } catch (DaoLayerException e) {
      log.error("Error updating OTP for email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to update OTP", e);
    }
  }

  /**
   * Deletes the OTP associated with the specified email address.
   *
   * @param email the email address whose OTP should be deleted
   */
  @Override
  public void delete(String email) throws ServiceLayerException {
    try {
      otpDao.deleteByEmail(email);
    } catch (DaoLayerException e) {
      throw new ServiceLayerException("Failed to delete OTP for email", e);
    }
  }
}
