package com.spring.security.service;

import com.spring.security.domain.entity.ActivityLog;
import com.spring.security.exceptions.ServiceLayerException;
import java.util.List;

/** Service interface for ActivityLog operations. */
public interface ActivityLogService {
  /**
   * Creates a new activity log entry.
   *
   * @param log the activity log to create
   * @return the created ActivityLog
   */
  ActivityLog create(ActivityLog log) throws ServiceLayerException;

  /**
   * Retrieves an activity log by its ID.
   *
   * @param id the log ID
   * @return the ActivityLog, or null if not found
   */
  ActivityLog findById(Long id) throws ServiceLayerException;

  /**
   * Retrieves all activity logs for a given user.
   *
   * @param userId the user ID
   * @return list of ActivityLog entries
   */
  List<ActivityLog> findByUserId(Long userId) throws ServiceLayerException;

  /**
   * Retrieves all activity logs for a given account.
   *
   * @param accountId the account ID
   * @return list of ActivityLog entries
   */
  List<ActivityLog> findByAccountId(Long accountId) throws ServiceLayerException;
}
