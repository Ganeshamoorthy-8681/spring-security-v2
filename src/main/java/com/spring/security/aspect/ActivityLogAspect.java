package com.spring.security.aspect;

import com.spring.security.annotation.LogActivity;
import com.spring.security.domain.entity.ActivityLog;
import com.spring.security.exceptions.ServiceLayerException;
import com.spring.security.service.ActivityLogService;
import com.spring.security.util.SecurityContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Aspect for automatically logging activities when methods are annotated with @LogActivity.
 */
@Aspect
@Component
@Slf4j
public class ActivityLogAspect {

    private final ActivityLogService activityLogService;

    public ActivityLogAspect(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @Around("@annotation(logActivity)")
    public Object logActivity(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {
        Object result = null;
        Throwable exception = null;
        Long entityId = null;

        try {
            // Proceed with actual method execution
            result = joinPoint.proceed();

            // Extract entity ID from the result if possible
            entityId = extractEntityId(result);

        } catch (Throwable e) {
            exception = e;
            if (!logActivity.logOnException()) {
                throw e; // don't log failures if not configured
            }
        }

        // Attempt to create activity log (silently fail if something goes wrong)
        try {
            createActivityLog(joinPoint, logActivity, entityId, exception);
        } catch (Exception ignore) {
            // swallow exception to not break business flow
        }

        if (exception != null) {
            throw exception;
        }

        return result;
    }

    private void createActivityLog(ProceedingJoinPoint joinPoint,
                                   LogActivity logActivity,
                                   Long entityId,
                                   Throwable exception) throws ServiceLayerException {

        ActivityLog activityLog = new ActivityLog();

        String userEmail = SecurityContextUtil.getCurrentUserEmail();
        Long accountId = SecurityContextUtil.getAccountIdFromContextOrPath();

        activityLog.setUserEmail(userEmail);
        activityLog.setAccountId(accountId);

        // Action (append _FAILED if exception occurred)
        String action = exception != null
                ? logActivity.action() + "_FAILED"
                : logActivity.action();
        activityLog.setAction(action);

        activityLog.setEntityType(logActivity.entityType());

        Long finalEntityId = determineEntityId(entityId, logActivity.entityType(), joinPoint);
        activityLog.setEntityId(finalEntityId);

        String description = logActivity.description().isEmpty()
                ? generateDescription(action, logActivity.entityType(), exception)
                : logActivity.description();
        activityLog.setDescription(description);

        activityLog.setIpAddress(getClientIpAddress());
        activityLog.setUserAgent(getUserAgent());
        activityLog.setCreatedAt(Instant.now());

        activityLogService.create(activityLog);
    }

    private Long determineEntityId(Long resultEntityId, String entityType, ProceedingJoinPoint joinPoint) {
        if (resultEntityId != null) {
            return resultEntityId;
        }

        return switch (entityType.toUpperCase()) {
            case "ACCOUNT" -> SecurityContextUtil.getAccountIdFromPathVariable();
            case "USER" -> SecurityContextUtil.getUserIdFromContextOrPath();
            case "ROLE" -> SecurityContextUtil.getRoleIdFromPathVariable();
            default -> extractEntityIdFromArguments(joinPoint);
        };
    }

    private Long extractEntityIdFromArguments(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Long id) {
                return id;
            }
            Long reflectedId = extractEntityId(arg);
            if (reflectedId != null) {
                return reflectedId;
            }
        }
        return null;
    }

    private Long extractEntityId(Object target) {
        if (target == null) return null;

        if (target instanceof Long id) {
            return id;
        }

        // Reflection check for "getId", "getUserId", "getAccountId"
        for (String methodName : new String[]{"getId", "getUserId", "getAccountId"}) {
            try {
                Method method = target.getClass().getMethod(methodName);
                Object value = method.invoke(target);
                if (value instanceof Long idValue) {
                    return idValue;
                }
            } catch (Exception ignored) {
                // method not found or failed, continue
            }
        }
        return null;
    }

    private String generateDescription(String action, String entityType, Throwable exception) {
        String status = exception != null ? "failed" : "completed";
        return String.format("%s %s %s", entityType, action.toLowerCase(), status);
    }

    private String getClientIpAddress() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                String xRealIp = request.getHeader("X-Real-IP");
                return (xRealIp != null && !xRealIp.isEmpty())
                        ? xRealIp
                        : request.getRemoteAddr();
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String getUserAgent() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                return attrs.getRequest().getHeader("User-Agent");
            }
        } catch (Exception ignored) {}
        return null;
    }
}
