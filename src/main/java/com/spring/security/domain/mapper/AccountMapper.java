package com.spring.security.domain.mapper;

import com.spring.security.controller.dto.request.AccountCreateRequestDto;
import com.spring.security.controller.dto.response.AccountResponseDto;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.entity.enums.AccountStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface AccountMapper {

  AccountMapper ACCOUNT_MAPPER = Mappers.getMapper(AccountMapper.class);

  /**
   * Converts an AccountCreateRequestDto to an Account entity.
   *
   * @param accountCreateRequest the DTO to convert
   * @return the converted Account entity
   */
  Account convertAccountCreateRequestToAccount(
      AccountCreateRequestDto accountCreateRequest, AccountStatus status);

  /**
   * Converts an Account entity to an AccountResponseDto.
   *
   * @param account the Account entity to convert
   * @return the converted AccountResponseDto
   */
  AccountResponseDto convertAccountToAccountResponseDto(Account account);
}
