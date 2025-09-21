package com.spring.security.controller.dto.response;

import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.domain.entity.enums.AccountType;
import com.spring.security.domain.entity.enums.UserStatus;

import java.time.Instant;

public class AccountGetResponseDto {


    /** The unique identifier of the account. */
    private Long id;

    /** The name of the account holder. */
    private String name;

    /** The description of the account holder. */
    private String description;

    /** The type of the account. */
    private AccountType type;

    /** The current status of the account. */
    private AccountStatus status;

        private String email;

        private String  firstName;

        private String lastName;

        private Instant currentLogin;

        private Instant lastLogin;

        private UserStatus userStatus;
}
