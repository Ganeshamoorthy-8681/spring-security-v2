package com.spring.security.domain.entity.enums;

/**
 * Enum representing the types of actions that can be performed on a permission.
 *
 * <p>This enum defines the following actions:
 *
 * <ul>
 *   <li>CREATE - Represents the creation of a resource.
 *   <li>READ - Represents reading or retrieving a resource.
 *   <li>UPDATE - Represents updating or modifying a resource.
 *   <li>DELETE - Represents deleting or removing a resource.
 * </ul>
 */
public enum PermissionActionType {
  /** Represents the creation of a resource. */
  CREATE,

  /** Represents reading or retrieving a resource. */
  READ,

  /** Represents updating or modifying a resource. */
  UPDATE,

  /** Represents deleting or removing a resource. */
  DELETE;
}
