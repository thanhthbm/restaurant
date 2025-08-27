package com.thanhthbm.restaurant.domain;

import com.thanhthbm.restaurant.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "dishes")
@DiscriminatorValue("DISH")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dish extends MenuItemBase{
    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String note;

}
