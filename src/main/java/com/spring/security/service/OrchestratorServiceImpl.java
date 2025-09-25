package com.spring.security.service;

import com.spring.security.controller.dto.request.AccountCreateRequestDto;
import com.spring.security.controller.dto.response.AccountCreateResponseDto;
import com.spring.security.controller.dto.response.AccountGetResponseDto;
import com.spring.security.controller.dto.response.OtpValidationStatus;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.mapper.AccountMapper;
import com.spring.security.domain.mapper.UserMapper;
import com.spring.security.exceptions.ServiceLayerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class that orchestrates operations involving both accounts and users.
 *
 * <p>This class provides methods to create an account along with its root user and to update the
 * status of an account and its associated user based on OTP validation results.
 */
@Slf4j
@Service
public class OrchestratorServiceImpl {

  private final AccountService accountService;

  private final UserService userService;

  /**
   * Constructor to initialize the OrchestratorService with required services.
   *
   * @param accountService the account service
   * @param userService the user service
   */
  public OrchestratorServiceImpl(AccountService accountService, UserService userService) {
    this.accountService = accountService;
    this.userService = userService;
  }

  /**
   * Creates an account and a root user associated with that account.
   *
   * @param accountCreateRequestDto the data transfer object containing account creation details
   * @return the created account response DTO
   * @throws ServiceLayerException if there is an error during account or user creation
   */
  @Transactional(rollbackFor = ServiceLayerException.class)
  public AccountCreateResponseDto createAccountWithRootUser(
      AccountCreateRequestDto accountCreateRequestDto) throws ServiceLayerException {
    Account account = accountService.create(accountCreateRequestDto);
    User user =
        userService.createRootUser(
            UserMapper.USER_MAPPER.convertAccountCreateRequestToRootUserCreateRequest(
                accountCreateRequestDto),
            account.getId());
    return AccountMapper.ACCOUNT_MAPPER.convertAccountAndUserToAccountCreateResponseDto(
        account, user.getEmail(), user.getId());
  }

  /**
   * Updates the status of an account and its associated user based on OTP validation results.
   *
   * <p>If the OTP validation status is VALID, the account status is updated to ACTIVE (if the user
   * is a root user) and the user status is updated to ACTIVE.
   *
   * @param accountId the ID of the account
   * @param email the email of the user
   * @param status the OTP validation status
   * @throws ServiceLayerException if there is an error during the update process
   */
  @Transactional(rollbackFor = ServiceLayerException.class)
  public void updateAccountAndUserStatusBasedOnOtpValidation(
      Long accountId, String email, OtpValidationStatus status) throws ServiceLayerException {
    try {
      if (status.equals(OtpValidationStatus.VALID)) {

        User user = userService.findByAccountIdAndEmail(accountId, email);
        if (isRootUser(user)) {
          log.info("Updating account status to ACTIVE with accountId: {}", accountId);
          accountService.updateStatus(accountId, AccountStatus.ACTIVE);
        }

        log.info("user status updated to ACTIVE for accountId: {} and email: {}", accountId, email);
        userService.updateUserStatus(accountId, email, UserStatus.ACTIVE);
      }
    } catch (ServiceLayerException e) {
      throw new ServiceLayerException(
          "Failed to update account and user status based on OTP validation", e);
    }
  }

  public AccountGetResponseDto getAccountAndRootUserByAccountId(Long accountId)
      throws ServiceLayerException {
    try {
      Account account = accountService.findById(accountId);
      User user = userService.findRootUserByAccountId(accountId);
      return AccountMapper.ACCOUNT_MAPPER.convertAccountAndUserToAccountGetResponseDto(
          account, user);
    } catch (ServiceLayerException e) {
      throw new ServiceLayerException("Failed to get account and root user by account id", e);
    }
  }

  /**
   * Checks if the user is a root user.
   *
   * @param user the user to check
   * @return true if the user is a root user, false otherwise
   */
  private boolean isRootUser(User user) {
    return user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("ROOT"));
  }
}
