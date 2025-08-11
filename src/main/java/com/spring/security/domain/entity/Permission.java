package com.spring.security.domain.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a permission entity used for access control. A permission defines an action that can
 * be performed on a resource. Example: - name: "IAM:USER:CREATE" - action: "CREATE" - description:
 * "Allows creation of user accounts"
 */
@Getter
@Setter
public class Permission {

  /** Unique identifier for the permission. This is typically a database-generated ID. */
  private Long id;

  /** The name of the permission. */
  private String name;

  /** A description of what the permission allows. */
  private String description;

  /**
   * The specific action allowed by this permission. Examples: "READ", "WRITE", "DELETE", "EXECUTE"
   */
  private String action;
}
