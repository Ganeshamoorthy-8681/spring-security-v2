package com.spring.security.controller;

import com.spring.security.controller.dto.request.AccountCreateRequestDto;
import com.spring.security.controller.dto.response.AccountResponseDto;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.mapper.AccountMapper;
import com.spring.security.exceptions.ServiceLayerException;
import com.spring.security.service.AccountService;
import com.spring.security.service.OrchestratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Account controller responsible for account based actions */
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

  private final AccountService accountService;

  private final OrchestratorService orchestratorService;

  /**
   * Constructor for AccountController.
   *
   * @param accountService the service to handle account-related operations
   */
  AccountController(AccountService accountService, OrchestratorService orchestratorService) {
    this.accountService = accountService;
    this.orchestratorService = orchestratorService;
  }

  /**
   * Retrieves account details by ID.
   *
   * @param id the ID of the account to retrieve
   * @return ResponseEntity containing the account details
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:ACCOUNT:READ')")
  public ResponseEntity<AccountResponseDto> getAccountDetails(@PathVariable Long id)
      throws ServiceLayerException {
    Account account = accountService.findById(id);
    return new ResponseEntity<>(
        com.spring.security.domain.mapper.AccountMapper.ACCOUNT_MAPPER
            .convertAccountToAccountResponseDto(account),
        HttpStatus.OK);
  }

  /**
   * Creates a new account.
   *
   * @param accountCreateRequestDto the request DTO containing account creation details
   * @return ResponseEntity with status CREATED
   */
  @PostMapping("/create")
  public ResponseEntity<AccountResponseDto> createAccount(
      @RequestBody AccountCreateRequestDto accountCreateRequestDto) throws ServiceLayerException {
    return new ResponseEntity<>(
        AccountMapper.ACCOUNT_MAPPER.convertAccountToAccountResponseDto(
            orchestratorService.createAccountWithRootUser(accountCreateRequestDto)),
        HttpStatus.CREATED);
  }

  /**
   * Deletes an account by ID.
   *
   * @param id the ID of the account to delete
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:ACCOUNT:DELETE')")
  public void deleteAccount(@PathVariable Long id) throws ServiceLayerException {
    accountService.delete(id);
  }
}
