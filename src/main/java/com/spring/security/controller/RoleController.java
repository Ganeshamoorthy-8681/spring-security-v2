package com.spring.security.controller;

import com.spring.security.controller.dto.request.RoleCreateRequestDto;
import com.spring.security.controller.dto.request.RoleUpdateRequestDto;
import com.spring.security.controller.dto.response.RoleResponseDto;
import com.spring.security.domain.entity.Role;
import com.spring.security.domain.mapper.RoleMapper;
import com.spring.security.exceptions.ServiceLayerException;
import com.spring.security.service.RoleService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Role controller user for managing roles in an account */
@RestController
@RequestMapping("/api/v1/accounts/{accountId}/roles")
public class RoleController {

  private final RoleService roleService;

  /**
   * Default constructor for RoleController. This can be used to inject dependencies if needed in
   * the future.
   */
  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  /**
   * Creates a new role.
   *
   * @return a message indicating the result of the operation
   */
  @PostMapping("/create")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:ROLE:CREATE')")
  public ResponseEntity<RoleResponseDto> create(
      @PathVariable Long accountId, @RequestBody RoleCreateRequestDto roleCreateRequestDto)
      throws ServiceLayerException {
    Role role = roleService.create(roleCreateRequestDto, accountId);

    return new ResponseEntity<>(
        RoleMapper.ROLE_MAPPER.convertRoleToResponseDto(role), HttpStatus.CREATED);
  }

  /**
   * Retrieves a role by its ID.
   *
   * @param accountId the unique identifier of the account
   * @param roleId the unique identifier of the role
   * @return a ResponseEntity containing the RoleResponseDto and HTTP status
   */
  @GetMapping("/{roleId}")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:ROLE:READ')")
  public ResponseEntity<RoleResponseDto> find(
      @PathVariable Long accountId, @PathVariable Long roleId) throws ServiceLayerException {
    Role role = roleService.findById(roleId, accountId);
    return new ResponseEntity<>(
        RoleMapper.ROLE_MAPPER.convertRoleToResponseDto(role), HttpStatus.OK);
  }

  /**
   * Retrieves a list of roles associated with a specific account ID.
   *
   * @param accountId the unique identifier of the account
   * @return a ResponseEntity containing a list of RoleResponseDto and HTTP status
   */
  @GetMapping("/list")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:ROLE:LIST')")
  public ResponseEntity<List<RoleResponseDto>> list(@PathVariable Long accountId)
      throws ServiceLayerException {
    List<Role> roles = roleService.list(accountId);
    return new ResponseEntity<>(
        RoleMapper.ROLE_MAPPER.convertRoleListToResponseDtoList(roles), HttpStatus.OK);
  }

  /**
   * Retrieves a role by its name.
   *
   * @param accountId the unique identifier of the account
   * @param name the name of the role
   * @return a ResponseEntity containing the RoleResponseDto and HTTP status
   */
  @GetMapping()
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:ROLE:READ')")
  public ResponseEntity<RoleResponseDto> findByName(
      @PathVariable Long accountId, @RequestParam String name) throws ServiceLayerException {
    Role role = roleService.findByName(name, accountId);
    return new ResponseEntity<>(
        RoleMapper.ROLE_MAPPER.convertRoleToResponseDto(role), HttpStatus.OK);
  }

  /**
   * Updates an existing role.
   *
   * @param accountId the unique identifier of the account
   * @param roleId the unique identifier of the role to be updated
   * @param roleUpdateRequestDto the DTO containing updated role information
   * @return a ResponseEntity containing the updated RoleResponseDto and HTTP status
   */
  @PutMapping("/{roleId}")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:ROLE:UPDATE')")
  public ResponseEntity<RoleResponseDto> updateRole(
      @PathVariable Long accountId,
      @PathVariable Long roleId,
      @RequestBody RoleUpdateRequestDto roleUpdateRequestDto)
      throws ServiceLayerException {
    Role updatedRole = roleService.update(roleId, accountId, roleUpdateRequestDto);
    return new ResponseEntity<>(
        RoleMapper.ROLE_MAPPER.convertRoleToResponseDto(updatedRole), HttpStatus.OK);
  }

  /**
   * Deletes an existing role.
   *
   * @param accountId the unique identifier of the account
   * @param roleId the unique identifier of the role to be deleted
   * @return a ResponseEntity indicating the result of the operation
   */
  @DeleteMapping("/{roleId}")
  @PreAuthorize("hasRole('ROOT') or hasAuthority('IAM:ROLE:DELETE')")
  public ResponseEntity<Void> deleteRole(@PathVariable Long accountId, @PathVariable Long roleId)
      throws ServiceLayerException {
    roleService.delete(roleId, accountId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
