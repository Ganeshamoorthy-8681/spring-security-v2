package com.spring.security.controller;

import com.spring.security.controller.dto.response.PermissionResponseDto;
import com.spring.security.domain.mapper.PermissionMapper;
import com.spring.security.exceptions.ServiceLayerException;
import com.spring.security.service.PermissionService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** PermissionController handles requests related to permissions. */
@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

  private final PermissionService permissionService;

  /**
   * Constructor for PermissionController.
   *
   * @param permissionService the service to handle permission-related operations
   */
  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  /**
   * Retrieves a list of all permissions.
   *
   * @return a list of PermissionResponseDto containing permission details
   */
  @GetMapping("/list")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:PERMISSION:LIST')")
  public List<PermissionResponseDto> getPermissionList() throws ServiceLayerException {
    return PermissionMapper.PERMISSION_MAPPER.convertPermissionToPermissionResponseDto(
        permissionService.list());
  }
}
