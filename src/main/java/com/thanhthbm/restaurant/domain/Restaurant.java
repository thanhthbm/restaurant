package com.thanhthbm.restaurant.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@jakarta.persistence.Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @OneToMany(mappedBy = "restaurant")
    private List<Table> tables;

    private double rating;

    private Instant createdAt;
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;
    private boolean active;
}
