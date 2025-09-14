package com.spring.security.util;

import com.spring.security.config.tokens.AccountUserAuthToken;
import com.spring.security.config.tokens.RootUserAuthToken;
import com.spring.security.domain.entity.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

/** Utility class for extracting authentication information from the security context. */
@Slf4j
public class SecurityContextUtil {

  /**
   * Extracts the account ID from the current security context. Returns null if no account ID is
   * available (e.g., for root users).
   */
  public static Long getCurrentAccountId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AccountUserAuthToken) {
      return ((AccountUserAuthToken) authentication).getAccountId();
    }

    // Try to get from CustomUserDetails if available
    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
        return userDetails.getAccountId();
    }

    return null;
  }

  /**
   * Intelligently gets the account ID from multiple sources:
   * 1. First tries to get from security context (authenticated user)
   * 2. Falls back to path parameter {accountId} from current request
   * 3. Returns null if neither is available
   */
  public static Long getAccountIdFromContextOrPath() {
    // First try to get from security context
    Long accountId = getCurrentAccountId();
    if (accountId != null) {
      log.debug("Account ID retrieved from security context: {}", accountId);
      return accountId;
    }

    // Fallback to path parameter
    accountId = getAccountIdFromPathVariable();
    if (accountId != null) {
      log.debug("Account ID retrieved from path parameter: {}", accountId);
      return accountId;
    }

    log.debug("No account ID found in security context or path parameters");
    return null;
  }

  /**
   * Extracts account ID from the current request path parameters.
   * Looks for {accountId} path variable in the current request.
   */
  public static Long getAccountIdFromPathVariable() {
    try {
      ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attrs != null) {
        HttpServletRequest request = attrs.getRequest();
        String requestURI = request.getRequestURI();

        // Extract accountId from path like /api/v1/accounts/{accountId}/...
        String[] pathSegments = requestURI.split("/");
        for (int i = 0; i < pathSegments.length; i++) {
          if ("accounts".equals(pathSegments[i]) && i + 1 < pathSegments.length) {
            String accountIdStr = pathSegments[i + 1];
            try {
              return Long.parseLong(accountIdStr);
            } catch (NumberFormatException e) {
              log.warn("Invalid account ID format in path: {}", accountIdStr);
              return null;
            }
          }
        }
      }
    } catch (Exception e) {
      log.warn("Error extracting account ID from path: {}", e.getMessage());
    }
    return null;
  }

  /**
   * Gets the current user ID from security context or path parameters.
   * First tries security context, then falls back to {userId} path parameter.
   */
  public static Long getUserIdFromContextOrPath() {
    // First try security context
    Long userId = getCurrentUserId();
    if (userId != null) {
      return userId;
    }

    // Fallback to path parameter
    return getUserIdFromPathVariable();
  }

  /**
   * Extracts user ID from the current request path parameters.
   * Looks for {userId} path variable in the current request.
   */
  public static Long getUserIdFromPathVariable() {
    try {
      ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attrs != null) {
        HttpServletRequest request = attrs.getRequest();
        String requestURI = request.getRequestURI();

        // Extract userId from path like /api/v1/accounts/{accountId}/users/{userId}
        String[] pathSegments = requestURI.split("/");
        for (int i = 0; i < pathSegments.length; i++) {
          if ("users".equals(pathSegments[i]) && i + 1 < pathSegments.length) {
            String userIdStr = pathSegments[i + 1];
            // Skip if it's a known endpoint suffix (not a user ID)
            if (!"profile".equals(userIdStr) && !"roles".equals(userIdStr) &&
                !"list".equals(userIdStr) && !"create".equals(userIdStr) &&
                !"email".equals(userIdStr) && !"set-password".equals(userIdStr) &&
                !"forgot-password".equals(userIdStr)) {
              try {
                return Long.parseLong(userIdStr);
              } catch (NumberFormatException e) {
                log.debug("Path segment '{}' is not a valid user ID", userIdStr);
                return null;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.warn("Error extracting user ID from path: {}", e.getMessage());
    }
    return null;
  }

  /**
   * Gets the current role ID from path parameters.
   * Looks for {roleId} path variable in the current request.
   */
  public static Long getRoleIdFromPathVariable() {
    try {
      ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attrs != null) {
        HttpServletRequest request = attrs.getRequest();
        String requestURI = request.getRequestURI();

        // Extract roleId from path like /api/v1/accounts/{accountId}/roles/{roleId}
        String[] pathSegments = requestURI.split("/");
        for (int i = 0; i < pathSegments.length; i++) {
          if ("roles".equals(pathSegments[i]) && i + 1 < pathSegments.length) {
            String roleIdStr = pathSegments[i + 1];
            // Skip if it's a known endpoint suffix (not a role ID)
            if (!"list".equals(roleIdStr) && !"create".equals(roleIdStr)) {
              try {
                return Long.parseLong(roleIdStr);
              } catch (NumberFormatException e) {
                log.debug("Path segment '{}' is not a valid role ID", roleIdStr);
                return null;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.warn("Error extracting role ID from path: {}", e.getMessage());
    }
    return null;
  }

  /**
   * Extracts the user email from the current security context. Returns null if no authentication is
   * present.
   */
  public static String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
        if (authentication.getPrincipal() instanceof String) {
            return authentication.getPrincipal().toString();
        }
      var userDetails = (CustomUserDetails) authentication.getPrincipal();
      return userDetails.getEmail();
    }
    return null;
  }

  /** Checks if the current user is a root user. */
  public static boolean isRootUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication instanceof RootUserAuthToken;
  }

  /** Gets the current authentication object. */
  public static Authentication getCurrentAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  /**
   * Gets the current user ID if available. This method attempts to extract the user ID from the
   * authentication principal.
   */
  public static Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getDetails() instanceof Long) {
      return (Long) authentication.getDetails();
    }
    return null;
  }

  /** Checks if there is an authenticated user in the current security context. */
  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.isAuthenticated();
  }
}
