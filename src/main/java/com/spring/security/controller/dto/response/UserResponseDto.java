package com.spring.security.controller.dto.response;

import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.entity.enums.UserType;

import java.time.Instant;
import java.time.LocalDateTime;
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

    /** The timestamp when the user was created. */
    private Instant createdAt;

    /** The timestamp when the user was last updated. */
    private Instant updatedAt;

    private Instant deletedAt;

    /** The identifier of the user who created this user record. */
    private String createdBy;

    /** The identifier of the user who last updated this user record. */
    private String updatedBy;

    /** The identifier of the user who deleted this user record, if applicable. */
    private String deletedBy;

    /** The timestamp of the user's current login. */
    private Instant currentLogin;

    /** The timestamp of the user's last login. */
    private Instant lastLogin;

    /** The number of failed login attempts. */
    private Integer failedLoginAttempts;

    /** The timestamp of the user's last failed login. */
    private Instant lastFailedLogin;

}
