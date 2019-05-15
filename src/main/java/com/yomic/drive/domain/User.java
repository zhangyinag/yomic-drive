package com.yomic.drive.domain;

import lombok.Data;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User{

    @Id
    private String username;

    private String cname;

    private String password;

    private String salt;

    private String ip;

    private boolean status;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "role_code"))
    private List<Role> roleList;
}
