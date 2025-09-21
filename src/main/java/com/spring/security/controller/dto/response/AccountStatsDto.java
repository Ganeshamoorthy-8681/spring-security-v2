package com.spring.security.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for account statistics.
 *
 * <p>This class is used to encapsulate various statistics related to an account, including total
 * users, active users, inactive users, failed login attempts, password expiring, pending
 * invitations, and MFA enabled users.
 */
@Getter
@Setter
public class AccountStatsDto {

    private Long totalUsers =0L;

    private Long activeUsers =0L;

    private Long inactiveUsers =0L;

    private Long failedLoginAttempts =0L;

    private Long passwordExpiring =0L;

    private Long pendingInvitations=0L;

    private Long mfaEnabled = 0L;
}
