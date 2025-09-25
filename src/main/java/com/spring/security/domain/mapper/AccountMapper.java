package com.spring.security.domain.mapper;

import com.spring.security.controller.dto.request.AccountCreateRequestDto;
import com.spring.security.controller.dto.response.AccountCreateResponseDto;
import com.spring.security.controller.dto.response.AccountGetResponseDto;
import com.spring.security.controller.dto.response.AccountResponseDto;
import com.spring.security.controller.dto.response.AccountStatsDto;
import com.spring.security.domain.entity.Account;
import com.spring.security.domain.entity.AccountStats;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.entity.enums.AccountStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    /**
     * Converts an AccountStats entity to an AccountStatsDto.
     *
     * @param accountStats the AccountStats entity to convert
     * @return the converted AccountStatsDto
     */
  AccountStatsDto convertAccountStatsToAccountStatsDto(AccountStats accountStats);


    /**
     * Converts an Account entity and a User entity to an AccountGetResponseDto.
     *
     * @param account the Account entity to convert
     * @param user the User entity to convert
     * @return the converted AccountGetResponseDto
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "status",source = "account.status")
    @Mapping(target = "userStatus",source = "user.status")
    @Mapping(target = "id",source = "account.id")
    @Mapping(target = "type",source = "account.type")
  AccountGetResponseDto convertAccountAndUserToAccountGetResponseDto(Account account, User user);

    /**
     * Converts an Account entity and a User entity to an AccountCreateResponseDto.
     * @param account the account entity
     * @return the converted AccountCreateResponseDto
     */
    AccountCreateResponseDto convertAccountAndUserToAccountCreateResponseDto(Account account, String email, Long userId);
}
