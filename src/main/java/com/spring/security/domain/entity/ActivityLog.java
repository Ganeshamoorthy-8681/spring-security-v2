package com.spring.security.domain.entity;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/** Represents an activity log entry in the system. */
@Getter
@Setter
public class ActivityLog {

  /** The unique identifier of the activity log. */
  private Long id;

  /** The user email */
  private String userEmail;

  /** The ID of the account associated with the action. */
  private Long accountId;

  /** The action performed. */
  private String action;

  /** The type of entity affected. */
  private String entityType;

  /** The ID of the entity affected. */
  private Long entityId;

  /** Description of the activity. */
  private String description;

  /** The IP address from which the action was performed. */
  private String ipAddress;

  /** The user agent string of the client. */
  private String userAgent;

  /** The timestamp when the activity was created. */
  private Instant createdAt;
}
