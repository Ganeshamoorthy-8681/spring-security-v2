package com.spring.security.service;

import com.spring.security.controller.dto.request.AccountCreateRequestDto;
import com.spring.security.controller.dto.response.AccountResponseDto;
import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.exceptions.ServiceLayerException;

public interface AccountService {

  /**
   * Creates a new account based on the provided request data.
   *
   * @param accountCreateRequestDto the data transfer object containing account creation details
   */
  AccountResponseDto create(AccountCreateRequestDto accountCreateRequestDto)
      throws ServiceLayerException;

  /**
   * Retrieves an account by its ID.
   *
   * @param id the unique identifier of the account
   * @return the account associated with the given ID
   */
  AccountResponseDto findById(Long id) throws ServiceLayerException;

  /**
   * Retrieves an account by its name.
   *
   * @param accountName the name of the account to retrieve
   * @return the account associated with the given name
   */
  AccountResponseDto findByAccountName(String accountName) throws ServiceLayerException;

  /**
   * Updates the status of an account.
   *
   * @param id the unique identifier of the account
   * @param status the new status to set for the account
   */
  void updateStatus(Long id, AccountStatus status) throws ServiceLayerException;

  /**
   * Deletes an account by its ID. (Soft delete)
   *
   * @param id the unique identifier of the account to be deleted
   */
  void delete(Long id) throws ServiceLayerException;
}
