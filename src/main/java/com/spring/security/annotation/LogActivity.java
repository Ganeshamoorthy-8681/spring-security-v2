package com.spring.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods that should log activities to the database. When a method is annotated
 * with @LogActivity, an aspect will automatically create an activity log entry with the specified
 * action and entity type.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogActivity {

  /** The action being performed (e.g., "CREATE", "UPDATE", "DELETE","LOGIN") */
  String action();

  /** The type of entity being operated on (e.g., "ACCOUNT", "USER", "ROLE", "PERMISSION") */
  String entityType();

  /**
   * Optional description of the activity. If not provided, a default description will be generated.
   */
  String description() default "";

  /** Whether to log even if the method throws an exception. Default is false. */
  boolean logOnException() default false;
}
