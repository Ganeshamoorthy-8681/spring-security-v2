package com.spring.security.service;

import com.spring.security.dao.ActivityLogDao;
import com.spring.security.domain.entity.ActivityLog;
import com.spring.security.exceptions.DaoLayerException;
import com.spring.security.exceptions.ServiceLayerException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

  private final ActivityLogDao activityLogDao;

  public ActivityLogServiceImpl(ActivityLogDao activityLogDao) {
    this.activityLogDao = activityLogDao;
  }

  @Override
  @Transactional(rollbackFor = ServiceLayerException.class)
  public ActivityLog create(ActivityLog log) throws ServiceLayerException {
    try {
      return activityLogDao.create(log);
    } catch (DaoLayerException e) {
      throw new ServiceLayerException("Failed to create activity log", e);
    }
  }

  @Override
  public ActivityLog findById(Long id) throws ServiceLayerException {
    try {
      return activityLogDao.findById(id);
    } catch (DaoLayerException e) {
      throw new ServiceLayerException("Failed to find activity log by id", e);
    }
  }

  @Override
  public List<ActivityLog> findByUserId(Long userId) throws ServiceLayerException {
    try {
      return activityLogDao.findByUserId(userId);
    } catch (DaoLayerException e) {
      throw new ServiceLayerException("Failed to find activity logs by userId", e);
    }
  }

  @Override
  public List<ActivityLog> findByAccountId(Long accountId) throws ServiceLayerException {
    try {
      return activityLogDao.findByAccountId(accountId);
    } catch (DaoLayerException e) {
      throw new ServiceLayerException("Failed to find activity logs by accountId", e);
    }
  }
}
