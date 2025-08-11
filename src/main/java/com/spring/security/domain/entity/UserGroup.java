package com.spring.security.domain.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a user group entity in the system.
 *
 * <p>This class contains the following fields:
 *
 * <ul>
 *   <li>id - The unique identifier of the user group.
 *   <li>name - The name of the user group.
 *   <li>description - A brief description of the user group.
 *   <li>users - A list of users that belong to the user group.
 *   <li>permissions - A list of permissions assigned to the user group.
 * </ul>
 */
@Getter
@Setter
public class UserGroup {

  /** The unique identifier of the user group. */
  private Long id;

  /** The name of the user group. */
  private String name;

  /** A brief description of the user group. */
  private String description;

  /** A list of users that belong to the user group. */
  //  private List<User> users;

  /** A list of permissions assigned to the user group. */
  //  private List<Permission> permissions;
}
