package com.honganhdev.lakesidehotel.service;
/*
 * @author HongAnh
 * @created 19 / 05 / 2024 - 9:11 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.honganhdev.lakesidehotel.exception.RoleAlreadyExistException;
import com.honganhdev.lakesidehotel.exception.UserAlreadyExistsException;
import com.honganhdev.lakesidehotel.model.Role;
import com.honganhdev.lakesidehotel.model.User;
import com.honganhdev.lakesidehotel.repository.RoleRepository;
import com.honganhdev.lakesidehotel.repository.UserRepository;
import com.honganhdev.lakesidehotel.service.serviceInterface.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if(roleRepository.existsByName(roleName)){
            throw new RoleAlreadyExistException(theRole.getName() + " role already existing");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Integer id) {
        this.removeAllUserFromRole(id);
        roleRepository.deleteById(id);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public User removeUserFromRole(Integer userId, Integer roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public User assignRoleToUser(Integer userId, Integer roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new UserAlreadyExistsException(user.get().getFirstName()+ " is already assign to the " + role.get().getName() + " role");
        }
        if(role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUserFromRole(Integer roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isPresent()){
            role.get().removeAllUserFromRole();
        }
        return roleRepository.save(role.get());
    }
}
