package com.spring.security.domain.mapper;

import com.spring.security.controller.dto.request.RoleCreateRequestDto;
import com.spring.security.controller.dto.response.RoleResponseDto;
import com.spring.security.domain.entity.Permission;
import com.spring.security.domain.entity.Role;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between Role entities and DTOs. This interface uses MapStruct to
 * generate the implementation at compile time.
 */
@Mapper
public interface RoleMapper {

  /** The singleton instance of RoleMapper. */
  RoleMapper ROLE_MAPPER = Mappers.getMapper(RoleMapper.class);

  /**
   * Converts a RoleCreateRequestDto to a Role entity.
   *
   * @param roleCreateRequestDto the request DTO containing role creation details
   * @param accountId the ID of the account to which the role belongs
   * @return the converted Role entity
   */
  Role convertCreateRequestToRole(
      RoleCreateRequestDto roleCreateRequestDto, Long accountId, List<Permission> permissions);

  /**
   * Converts a Role entity to a RoleResponseDto.
   *
   * @param role the Role entity to convert
   * @return the converted RoleResponseDto
   */
  RoleResponseDto convertRoleToResponseDto(Role role);

  /**
   * Converts a list of Role entities to a list of RoleResponseDto.
   *
   * @param roles the list of Role entities to convert
   * @return the list of converted RoleResponseDto
   */
  List<RoleResponseDto> convertRoleListToResponseDtoList(List<Role> roles);

  /**
   * Converts a RoleResponseDto to a Role entity.
   *
   * @return the converted Role entity
   */
  Role convertRoleResponseDtoToRole(RoleResponseDto roleResponseDto);
}
