package com.spring.security.domain.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents various statistics related to an account.
 *
 * <p>This class is used to encapsulate various statistics related to an account, including total
 * users, active users, inactive users, failed login attempts, password expiring, pending
 * invitations, and MFA enabled users.
 */
@Getter
@Setter
public class AccountStats {

    private Long totalUsers = 0L;

    private Long activeUsers = 0L;

    private Long inactiveUsers = 0L;

    private Long failedLoginAttempts = 0L;

    private Long passwordExpiring = 0L;

    private Long pendingInvitations = 0L;

    private Long mfaEnabled = 0L;
}
