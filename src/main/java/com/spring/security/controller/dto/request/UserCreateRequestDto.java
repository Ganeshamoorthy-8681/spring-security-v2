package com.spring.security.controller.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new user account.
 *
 * <p>This class contains the necessary fields to create a new user, including email, password,
 * first name, last name, middle name, and description.
 */
@Getter
@Setter
public class UserCreateRequestDto {

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

  /** The Roles assigned to the user. */
  private List<UserRoleRequestDto> roles;
}
