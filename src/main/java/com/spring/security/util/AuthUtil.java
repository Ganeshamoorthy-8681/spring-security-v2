package com.spring.security.util;

import com.spring.security.domain.entity.Permission;
import com.spring.security.domain.entity.Role;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/** Utility class for handling authentication-related operations. */
public final class AuthUtil {

  /**
   * Extracts and constructs a list of granted authorities from the user's roles and permissions.
   *
   * @param roles the collection of roles assigned to the user
   * @return a list of SimpleGrantedAuthority objects representing the user's roles and permissions
   */
  public static List<SimpleGrantedAuthority> getAuthorities(Collection<Role> roles) {

    return roles.stream()
        .flatMap(
            role ->
                Stream.concat(
                    Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getName())),
                    role.getPermissions().stream()
                        .map(Permission::getName)
                        .map(SimpleGrantedAuthority::new)))
        .distinct()
        .toList();
  }
}
