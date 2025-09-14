package com.spring.security.controller;

import com.spring.security.controller.dto.request.OtpResendRequestDto;
import com.spring.security.controller.dto.request.OtpValidateRequestDto;
import com.spring.security.controller.dto.response.OtpValidateResponseDto;
import com.spring.security.exceptions.ServiceLayerException;
import com.spring.security.service.OrchestratorService;
import com.spring.security.service.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/otp")
public class OtpController {

  private final OtpService otpService;

  private final OrchestratorService orchestratorService;

  /**
   * Constructor for OtpController.
   *
   * @param otpService the UserService to be used by this controller
   */
  public OtpController(OtpService otpService, OrchestratorService orchestratorService) {
    this.otpService = otpService;
    this.orchestratorService = orchestratorService;
  }

  /**
   * Validates an OTP for a user.
   *
   * @param requestDto the request data transfer object containing the OTP to validate
   * @return a ResponseEntity indicating the result of the validation
   */
  @PostMapping("/validate")
  public ResponseEntity<OtpValidateResponseDto> validateOtp(
      @RequestBody OtpValidateRequestDto requestDto) throws ServiceLayerException {

    OtpValidateResponseDto response = otpService.validateOtp(requestDto);

    orchestratorService.updateAccountAndUserStatusBasedOnOtpValidation(
        requestDto.getAccountId(), requestDto.getEmail(), response.getStatus());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Resends an OTP to the user's email.
   *
   * @param requestDto the request data transfer object containing the user's email
   * @return a ResponseEntity indicating that the OTP has been resent
   */
  @PostMapping("/resend")
  public ResponseEntity<Void> resendOtp(@RequestBody OtpResendRequestDto requestDto)
      throws ServiceLayerException {
    otpService.resendOtp(requestDto.getEmail());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
