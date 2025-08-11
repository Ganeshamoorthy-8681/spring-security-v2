package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/** Root user creation request DTO. */
@Getter
@Setter
public class RootUserCreateRequestDto {
  /** The email address of the user. */
  private String email;

  /** The First name of the user. */
  private String firstName;

  /** The Last name of the user. */
  private String lastName;

  /** The Middle name of the user, if applicable. */
  private String middleName;

  /** The description for the user account. */
  private String description;
}
