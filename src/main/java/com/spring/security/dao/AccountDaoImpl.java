package com.spring.security.dao;

import com.spring.security.dao.mapper.AccountMapper;
import com.spring.security.domain.entity.Account;
import java.util.Map;

import com.spring.security.exceptions.DaoLayerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountDaoImpl implements AccountDao {

  private final AccountMapper accountMapper;

  /**
   * Constructor for AccountDaoImpl.
   *
   * @param accountMapper the AccountMapper to be used for database operations
   */
  @Autowired
  public AccountDaoImpl(AccountMapper accountMapper) {
    this.accountMapper = accountMapper;
  }

  /**
   * Creates a new account.
   *
   * @param account the account to be created
   */
  @Override
  public Account create(Account account) throws DaoLayerException {

    try {
      int rowCount = accountMapper.create(account);
      if(rowCount < 1){
        throw new DaoLayerException("Failed to create account");
      }
      return account;
    } catch (Exception e) {
      log.error("Error creating account: {}", e.getMessage());
      throw new DaoLayerException("Failed to create account",e);
    }
  }

  /**
   * Retrieves an account by its ID.
   *
   * @param id the ID of the account to retrieve
   * @return the account with the specified ID, or null if not found
   */
  @Override
  public Account findById(Long id) throws DaoLayerException {
    try {
      return accountMapper.findById(id);
    } catch (Exception e) {
      log.error("Error retrieving account with ID {}: {}", id, e.getMessage());
      throw new DaoLayerException("Failed to retrieve account",e);
    }
  }

  /**
   * Retrieves an account by its name.
   *
   * @param accountName the name of the account to retrieve
   * @return the account with the specified name, or null if not found
   */
  @Override
  public Account findByName(String accountName) throws DaoLayerException {
    try {
      return accountMapper.findByName(accountName);
    } catch (Exception e) {
      log.error("Error retrieving account with name {}: {}", accountName, e.getMessage());
      throw new DaoLayerException("Failed to retrieve account by name",e);
    }
  }

  /**
   * Updates an existing account.
   *
   * @param updates a map containing the fields to be updated and their new values
   * @param conditions a map containing the conditions for the update operation
   */
  @Override
  public void update(Map<String, Object> updates, Map<String, Object> conditions) throws DaoLayerException {

    try {
      int rowCount = accountMapper.update("accounts", updates, conditions);

      if (rowCount < 1) {
          throw new DaoLayerException("Failed to update account");
      }

    } catch (Exception e) {
      log.error("Error updating account: {}", e.getMessage());
      throw new DaoLayerException("Failed to update account",e);
    }
  }
}
