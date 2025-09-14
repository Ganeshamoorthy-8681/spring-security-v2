package com.spring.security.dao;

import com.spring.security.dao.mapper.ActivityLogMapper;
import com.spring.security.domain.entity.ActivityLog;
import com.spring.security.exceptions.DaoLayerException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ActivityLogDaoImpl implements ActivityLogDao {

  private final ActivityLogMapper activityLogMapper;

  public ActivityLogDaoImpl(ActivityLogMapper activityLogMapper) {
    this.activityLogMapper = activityLogMapper;
  }

  @Override
  @Transactional(rollbackFor = DaoLayerException.class)
  public ActivityLog create(ActivityLog log) throws DaoLayerException {
    activityLogMapper.insert(log);
    return log;
  }

  @Override
  public ActivityLog findById(Long id) throws DaoLayerException {
    return activityLogMapper.findById(id);
  }

  @Override
  public List<ActivityLog> findByUserId(Long userId) throws DaoLayerException {
    return activityLogMapper.findByUserId(userId);
  }

  @Override
  public List<ActivityLog> findByAccountId(Long accountId) throws DaoLayerException {
    return activityLogMapper.findByAccountId(accountId);
  }
}
