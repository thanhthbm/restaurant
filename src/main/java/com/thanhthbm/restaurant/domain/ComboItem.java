package com.thanhthbm.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
        name = "combo_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"combo_id", "dish_id"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComboItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "combo_id")
    private Combo combo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Column(nullable = false)
    @NotNull(message = "quantity must not be null")
    private int quantity;
}
