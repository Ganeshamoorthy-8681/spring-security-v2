package com.spring.security.domain.entity;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a role entity in the system.
 *
 * <p>This class contains the following fields:
 *
 * <ul>
 *   <li>id - The unique identifier of the role.
 *   <li>name - The name of the role.
 *   <li>description - A brief description of the role.
 *   <li>accountId - The ID of the account associated with the role.
 *   <li>permissions - A list of permissions associated with the role.
 * </ul>
 */
@Getter
@Setter
public class Role {

  /** The unique identifier of the role. */
  private Long id;

  /** The name of the role. */
  private String name;

  /** A brief description of the role. */
  private String description;

  /** The ID of the account associated with the role. */
  private Long accountId;

  /** A list of permissions associated with the role. */
  private List<Permission> permissions;

  /** Creation timestamp of the role. */
  private Instant createdAt;

  /** Last update timestamp of the role. */
  private Instant updatedAt;

  /** Created by user identifier. */
  private String createdBy;

  /** Updated by user identifier. */
  private String updatedBy;
}
