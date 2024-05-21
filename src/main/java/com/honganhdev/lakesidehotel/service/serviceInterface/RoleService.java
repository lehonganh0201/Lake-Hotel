package com.honganhdev.lakesidehotel.service.serviceInterface;
/*
 * @author HongAnh
 * @created 19 / 05 / 2024 - 8:59 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.honganhdev.lakesidehotel.model.Role;
import com.honganhdev.lakesidehotel.model.User;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();
    Role createRole(Role theRole);
    void deleteRole(Integer id);
    Role findByName(String name);
    User removeUserFromRole(Integer userId, Integer roleId);
    User assignRoleToUser(Integer userId, Integer roleId);
    Role removeAllUserFromRole(Integer roleId);
}
