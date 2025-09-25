package com.spring.security.service;

import com.spring.security.component.OtpGenerator;
import com.spring.security.controller.dto.request.OtpValidateRequestDto;
import com.spring.security.controller.dto.response.OtpValidateResponseDto;
import com.spring.security.controller.dto.response.OtpValidationStatus;
import com.spring.security.dao.OtpDao;
import com.spring.security.domain.entity.OtpCode;
import com.spring.security.exceptions.DaoLayerException;
import com.spring.security.exceptions.OtpGenerationFailedException;
import com.spring.security.exceptions.ResourceNotFoundException;
import com.spring.security.exceptions.ServiceLayerException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for handling OTP (One-Time Password) operations. This service provides
 * methods to create, find, update, delete, and validate OTPs.
 */
@Service
@Slf4j
public class OtpServiceImpl implements OtpService {

  private final OtpDao otpDao;

  private final OtpGenerator otpGenerator;

  /** Constructor for OtpServiceImpl. */
  public OtpServiceImpl(OtpDao otpDao, OtpGenerator otpGenerator) {
    this.otpDao = otpDao;
    this.otpGenerator = otpGenerator;
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
      otpCode.setCreatedAt(Instant.now());
      otpCode.setExpiresAt(Instant.now().plus(Duration.ofMinutes(3))); // OTP valid for 3 minutes
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

  /**
   * Verifies the OTP code for an account.
   *
   * @param otpRequestDto the request dto for verifying the OTP
   */
  @Override
  @Transactional(rollbackFor = ServiceLayerException.class)
  public OtpValidateResponseDto validateOtp(OtpValidateRequestDto otpRequestDto)
      throws ServiceLayerException {

    try {
      OtpCode otpCode = find(otpRequestDto.getEmail());
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
  private OtpValidateResponseDto processOtpValidation(
      OtpValidateRequestDto request, OtpCode otpCode) throws ServiceLayerException {
    OtpValidateResponseDto response = new OtpValidateResponseDto();

    if (otpCode == null) {
      log.warn("No OTP found for email");
      response.setStatus(OtpValidationStatus.NOT_FOUND);
      return response;
    }

    if (otpCode.getExpiresAt().isBefore(Instant.now())) {
      log.warn("OTP has expired");
      response.setStatus(OtpValidationStatus.EXPIRED);
      return response;
    }

    if (!otpCode.getOtp().equals(String.valueOf(request.getOtp()))) {
      response.setStatus(OtpValidationStatus.INVALID);
      return response;
    }
    response.setStatus(OtpValidationStatus.VALID);
    markOtpAsUsed(otpCode);
    log.info("OTP validated successfully for email");
    return response;
  }

  /**
   * Marks the OTP as used by deleting it from the database.
   *
   * @param otpCode the OTP code to mark as used
   */
  private void markOtpAsUsed(OtpCode otpCode) throws ServiceLayerException {
    try {
      Map<String, Object> conditionsMap = new HashMap<>();
      conditionsMap.put("email", otpCode.getEmail());
      conditionsMap.put("otp", otpCode.getOtp());
      Map<String, Object> updateMap = new HashMap<>();
      updateMap.put("used", "true");
      otpDao.update(updateMap, conditionsMap);
      log.info("OTP marked as used for email {}", otpCode.getEmail());
    } catch (DaoLayerException e) {
      log.error("Error marking OTP as used for email {}: {}", otpCode.getEmail(), e.getMessage());
      throw new ServiceLayerException("Failed to mark OTP as used", e);
    }
  }

  /**
   * Generates and stores a new OTP for the specified email address.
   *
   * @param email the email address to generate OTP for
   * @return the generated OTP string
   * @throws ServiceLayerException if OTP generation or storage fails
   */
  @Override
  public String generateAndStoreOtp(String email) throws ServiceLayerException {
    try {
      String otp = otpGenerator.generateOtp();
      if (otp == null || otp.isBlank()) {
        throw new OtpGenerationFailedException("Failed to generate OTP");
      }

      // Check if OTP already exists for this email
      try {
        OtpCode existingOtp = find(email);
        if (existingOtp != null) {
          // Update existing OTP
          update(email, otp);
        }
      } catch (ResourceNotFoundException e) {
        // Create new OTP if none exists
        create(email, otp);
      }

      return otp;
    } catch (Exception e) {
      log.error("Failed to generate and store OTP for email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to generate OTP", e);
    }
  }
}
