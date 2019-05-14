package com.yomic.drive.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    private String username;

    private String cname;

    private String password;

    private String salt;

    private String ip;

    private boolean status;

    @OneToMany()
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "role_code"))
    private List<Role> roleList;
}
