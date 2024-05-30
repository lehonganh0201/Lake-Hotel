package com.honganhdev.lakesidehotel.controller;
/*
 * @author HongAnh
 * @created 19 / 05 / 2024 - 9:52 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.honganhdev.lakesidehotel.exception.RoleAlreadyExistException;
import com.honganhdev.lakesidehotel.model.Role;
import com.honganhdev.lakesidehotel.model.User;
import com.honganhdev.lakesidehotel.service.serviceInterface.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping()
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRole(@RequestBody Role role){
        try{
            roleService.createRole(role);
            return ResponseEntity.ok("New role created successfully!");
        }catch (RoleAlreadyExistException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Integer roleId){
        roleService.deleteRole(roleId);
    }

    @PostMapping("/remove-all-user/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Integer roleId){
        return roleService.removeAllUserFromRole(roleId);
    }

    @PostMapping("/remove-user")
    public User removeUserFromRole(@RequestParam("userId") Integer userId,
                                   @RequestParam("roleId") Integer roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assign")
    public User assignRoleToUser(@RequestParam("userId") Integer userId,
                                 @RequestParam("roleId") Integer roleId){
        return roleService.assignRoleToUser(userId, roleId);
    }
}
