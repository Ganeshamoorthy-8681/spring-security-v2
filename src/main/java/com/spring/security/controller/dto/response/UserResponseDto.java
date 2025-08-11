package com.spring.security.controller.dto.response;

import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.entity.enums.UserType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

  /** The unique identifier of the user. */
  private Long id;

  /** The first name of the account holder. */
  private String firstName;

  /** The last name of the account holder. */
  private String lastName;

  /** The middle name of the account holder, if applicable. */
  private String middleName;

  /** The email address. */
  private String email;

  /** The type of the user, represented by the {@link UserType} enum. */
  private UserType type;

  /** The ID of the account associated with the user. */
  private Long accountId;

  /** The current status of the user, represented by the {@link UserStatus} enum. */
  private UserStatus status;

  /** The list of roles assigned to the user. */
  private List<UserRoleResponseDto> roles;
}
