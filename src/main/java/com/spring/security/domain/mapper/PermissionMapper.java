package com.spring.security.domain.mapper;

import com.spring.security.controller.dto.request.PermissionDto;
import com.spring.security.controller.dto.response.PermissionResponseDto;
import com.spring.security.domain.entity.Permission;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting Permission entities to PermissionResponseDto. Utilizes MapStruct
 * for automatic mapping.
 */
@Mapper
public interface PermissionMapper {

  PermissionMapper PERMISSION_MAPPER = Mappers.getMapper(PermissionMapper.class);

  /**
   * Converts a list of Permission entities to a list of PermissionResponseDto.
   *
   * @param permissions the list of Permission entities to convert
   * @return the converted list of PermissionResponseDto
   */
  List<PermissionResponseDto> convertPermissionToPermissionResponseDto(
      List<Permission> permissions);

  /**
   * Converts a list of PermissionResponseDto to a list of Permission entities.
   *
   * @param permissionDtos the list of PermissionResponseDto to convert
   * @return the converted list of Permission entities
   */
  List<Permission> convertPermissionDtoToPermission(List<PermissionDto> permissionDtos);
}
