package com.honganhdev.lakesidehotel.model;
/*
 * @author HongAnh
 * @created 17 / 05 / 2024 - 10:53 PM
 * @project lakeSide-hotel
 * @social Github: https://github.com/lehonganh0201
 * @social Facebook: https://www.facebook.com/profile.php?id=100047152174225
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<User> users = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public void assignRoleToUser(User user){
        user.getRoles().add(this);
        this.users.add(user);
    }

    public void removeUserFromRole(User user){
        user.getRoles().remove(this);
        this.users.remove(user);
    }

    public void removeAllUserFromRole(){
        if(this.users != null){
            List<User> roleUsers = this.users.stream().toList();
            roleUsers.forEach(this::removeUserFromRole);
        }
    }

    public String getName(){
        return name != null ? name : "";
    }
}
