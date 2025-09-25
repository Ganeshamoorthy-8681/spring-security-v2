package com.spring.security.domain.mapper;

import com.spring.security.controller.dto.request.AccountCreateRequestDto;
import com.spring.security.controller.dto.request.RootUserCreateRequestDto;
import com.spring.security.controller.dto.request.UserCreateRequestDto;
import com.spring.security.controller.dto.response.UserCreateResponseDto;
import com.spring.security.controller.dto.response.UserResponseDto;
import com.spring.security.domain.entity.Role;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.entity.enums.UserType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting dto's to User entity and vice versa. This interface uses
 * MapStruct to generate the implementation at compile time.
 */
@Mapper
public interface UserMapper {

  /** Singleton instance of UserMapper */
  UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

  /**
   * Converts a UserCreateRequestDto to a User entity.
   *
   * @param userRequestDTO the UserCreateRequestDto containing user creation details
   * @param status the status to set for the user
   * @return a User entity populated with the details from the UserCreateRequestDto
   */
  User convertUserCreateRequestDtoToUser(
      UserCreateRequestDto userRequestDTO, UserType type, UserStatus status, Long accountId);

  /**
   * Converts a RootUserCreateRequestDto to a User entity.
   *
   * @param userRequestDTO the RootUserCreateRequestDto containing root user creation details
   * @param type the type of the user
   * @param status the status to set for the user
   * @param accountId the account ID associated with the user
   * @param roles the list of roles assigned to the user
   * @return a User entity populated with the details from the RootUserCreateRequestDto
   */
  User convertRootUserCreateRequestDtoToUser(
      RootUserCreateRequestDto userRequestDTO,
      UserType type,
      UserStatus status,
      Long accountId,
      List<Role> roles,
      Boolean isRoot);

  /**
   * Converts a User entity to a UserResponseDto.
   *
   * @param user the User entity to convert
   * @return a UserResponseDto containing the details of the User entity
   */
  UserResponseDto convertUserToUserResponseDto(User user);

  /**
   * Converts a User entity to a UserCreateResponseDto.
   *
   * @param user the User entity to convert
   * @return a UserCreateResponseDto containing the details of the User entity
   */
  UserCreateResponseDto convertUserToUserCreateResponseDto(User user);

  /**
   * convert a accountCreateRequestDto to rootUserCreateRequestDto
   *
   * @param accountCreateRequestDto the AccountCreateRequestDto containing account creation details
   * @return UserCreateRequestDto
   */
  @Mapping(source = "userDescription", target = "description")
  RootUserCreateRequestDto convertAccountCreateRequestToRootUserCreateRequest(
      AccountCreateRequestDto accountCreateRequestDto);
}
