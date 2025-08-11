package com.spring.security.dao;

import com.spring.security.domain.entity.Account;
import com.spring.security.exceptions.DaoLayerException;

import java.util.Map;

/**
 * AccountDao interface for managing account-related operations. This interface defines methods for
 * creating and retrieving accounts.
 */
public interface AccountDao {

  /**
   * Creates a new account.
   *
   * @param account the account to be created
   */
  Account create(Account account) throws DaoLayerException;

  /**
   * Retrieves an account by its ID.
   *
   * @param id the ID of the account to retrieve
   * @return the account with the specified ID, or null if not found
   */
  Account findById(Long id) throws DaoLayerException;


  /**
   * Retrieves an account by its name.
   *
   * @param accountName the name of the account to retrieve
   * @return the account with the specified name, or null if not found
   */
  Account findByName(String accountName) throws DaoLayerException;

  /**
   * Updates an existing account.
   *
   * @param updates a map containing the fields to be updated and their new values
   * @param conditions a map containing the conditions for the update operation
   */
  void update(Map<String, Object> updates, Map<String, Object> conditions) throws DaoLayerException;
}
