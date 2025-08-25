package com.thanhthbm.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "role name cannot be blank")
    private String name;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JsonIgnoreProperties(value = {"roles"})
//    @JoinTable(
//            name = "permission_role",
//            joinColumns = @JoinColumn(name = "role_id"),
//            inverseJoinColumns = @JoinColumn(name = "permission_id")
//    )
//    private List<Permission> permissions;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonIgnore
    List<User> users;

}
