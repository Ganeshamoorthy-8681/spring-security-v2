package com.spring.security.dao;

import com.spring.security.domain.entity.ActivityLog;
import com.spring.security.exceptions.DaoLayerException;
import java.util.List;

/** DAO interface for ActivityLog entity. */
public interface ActivityLogDao {
  /**
   * Persists a new activity log entry.
   *
   * @param log the activity log to create
   * @return the created ActivityLog with generated ID
   */
  ActivityLog create(ActivityLog log) throws DaoLayerException;

  /**
   * Retrieves an activity log by its ID.
   *
   * @param id the log ID
   * @return the ActivityLog, or null if not found
   */
  ActivityLog findById(Long id) throws DaoLayerException;

  /**
   * Retrieves all activity logs for a given user.
   *
   * @param userId the user ID
   * @return list of ActivityLog entries
   */
  List<ActivityLog> findByUserId(Long userId) throws DaoLayerException;

  /**
   * Retrieves all activity logs for a given account.
   *
   * @param accountId the account ID
   * @return list of ActivityLog entries
   */
  List<ActivityLog> findByAccountId(Long accountId) throws DaoLayerException;
}
