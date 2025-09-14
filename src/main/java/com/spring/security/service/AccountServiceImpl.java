package com.spring.security.service;

import com.spring.security.annotation.LogActivity;
import com.spring.security.controller.dto.request.AccountCreateRequestDto;
import com.spring.security.dao.AccountDao;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.domain.mapper.AccountMapper;
import com.spring.security.exceptions.DaoLayerException;
import com.spring.security.exceptions.ResourceAlreadyExistException;
import com.spring.security.exceptions.ResourceNotFoundException;
import com.spring.security.exceptions.ServiceLayerException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

  private final AccountDao accountDao;

  /**
   * Constructs an AccountServiceImpl with the necessary dependencies.
   *
   * @param accountDao the data access object for account operations
   */
  public AccountServiceImpl(AccountDao accountDao, UserService userService) {
    this.accountDao = accountDao;
  }

  /**
   * Creates a new account based on the provided request data.
   *
   * @param accountCreateRequestDto the data transfer object containing account creation details
   */
  @Override
  @LogActivity(action = "CREATE", entityType = "ACCOUNT", description = "New account created")
  public Account create(AccountCreateRequestDto accountCreateRequestDto)
      throws ServiceLayerException {

    try {

      Account account =
          AccountMapper.ACCOUNT_MAPPER.convertAccountCreateRequestToAccount(
              accountCreateRequestDto, AccountStatus.CREATED);
      if (isAccountExists(account.getName())) {
        log.warn("Account with name '{}' already exists", account.getName());
        throw new ResourceAlreadyExistException("Account with this name already exists");
      }
      accountDao.create(account);
      return account;
    } catch (DaoLayerException e) {
      log.error("Error creating account: {}", e.getMessage());
      throw new ServiceLayerException("Failed to create account", e);
    }
  }

  /**
   * Retrieves an account by its name.
   *
   * @param accountName the name of the account to retrieve
   * @return the account associated with the given name
   * @throws ServiceLayerException if an error occurs while retrieving the account
   */
  @Override
  public Account findByAccountName(String accountName) throws ServiceLayerException {
    try {
      Account account = accountDao.findByName(accountName);

      if (account == null) {
        log.warn("Account with name '{}' not found", accountName);
        throw new ResourceNotFoundException("Account not found");
      }

      return account;
    } catch (DaoLayerException e) {
      log.error("Error finding account by name: {}", e.getMessage());
      throw new ServiceLayerException("Failed to find account by name", e);
    }
  }

  /**
   * Checks if an account with the given name already exists.
   *
   * @param accountName the name of the account to check
   * @return true if the account exists, false otherwise
   */
  private boolean isAccountExists(String accountName) {
    try {
      findByAccountName(accountName);
      return true;
    } catch (ServiceLayerException e) {
      log.error("Error checking if account exists: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Retrieves an account by its ID.
   *
   * @param id the unique identifier of the account
   * @return the account associated with the given ID
   */
  @Override
  public Account findById(Long id) throws ServiceLayerException {
    try {
      Account account = accountDao.findById(id);
      if (account == null) {
        log.warn("Account with ID '{}' not found", id);
        throw new ResourceNotFoundException("Account not found");
      }
      return account;
    } catch (DaoLayerException e) {
      log.error("Error finding account by ID: {}", e.getMessage());
      throw new ServiceLayerException("Failed to find account by ID", e);
    }
  }

  /**
   * Updates the status of an account.
   *
   * @param accountId the unique identifier of the account
   * @param status the new status to set for the account
   */
  @Override
  @LogActivity(action = "UPDATE", entityType = "ACCOUNT", description = "Account status updated")
  public void updateStatus(Long accountId, AccountStatus status) throws ServiceLayerException {
    try {
      Map<String, Object> updateMap = Map.of("status", status.toString());
      Map<String, Object> conditionMap = Map.of("id", accountId);
      accountDao.update(updateMap, conditionMap);
    } catch (DaoLayerException e) {
      log.error("Error updating account status: {}", e.getMessage());
      throw new ServiceLayerException("Failed to update account status", e);
    }
  }

  /**
   * Deletes an account by its ID. (Soft delete)
   *
   * @param id the unique identifier of the account to be deleted
   */
  @Override
  @LogActivity(action = "DELETE", entityType = "ACCOUNT", description = "Account deleted")
  public void delete(Long id) throws ServiceLayerException {
    updateStatus(id, AccountStatus.DELETED);
  }
}
