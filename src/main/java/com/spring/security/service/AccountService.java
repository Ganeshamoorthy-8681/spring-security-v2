package com.spring.security.service;

import com.spring.security.controller.dto.request.AccountCreateRequestDto;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.entity.AccountStats;
import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.exceptions.ServiceLayerException;

public interface AccountService {

  /**
   * Creates a new account based on the provided request data.
   *
   * @param accountCreateRequestDto the data transfer object containing account creation details
   */
  Account create(AccountCreateRequestDto accountCreateRequestDto) throws ServiceLayerException;

  /**
   * Retrieves an account by its ID.
   *
   * @param id the unique identifier of the account
   * @return the account associated with the given ID
   */
  Account findById(Long id) throws ServiceLayerException;

  /**
   * Retrieves an account by its name.
   *
   * @param accountName the name of the account to retrieve
   */
  void findByAccountName(String accountName) throws ServiceLayerException;

  /**
   * Updates the status of an account.
   *
   * @param id the unique identifier of the account
   * @param status the new status to set for the account
   */
  void updateStatus(Long id, AccountStatus status) throws ServiceLayerException;


  /**
   * Gathers and processes statistics for the account with the given ID.
   *
   * @param id the unique identifier of the account
   * @return AccountStats containing various metrics for the account
   */
  AccountStats getAccountStats(Long id) throws ServiceLayerException;


  /**
   * Deletes an account by its ID. (Soft delete)
   *
   * @param id the unique identifier of the account to be deleted
   */
  void delete(Long id) throws ServiceLayerException;
}
