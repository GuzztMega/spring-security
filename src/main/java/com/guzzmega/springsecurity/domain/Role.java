package com.guzzmega.springsecurity.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String name;

    public enum Type {
        ADMIN(1L),
        BASIC(2L);

        long idRole;

        Type(long idRole) {
            this.idRole = idRole;
        }

        public long getIdRole() {
            return idRole;
        }
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
