package com.thanhthbm.restaurant.domain;

import com.thanhthbm.restaurant.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "menu_items")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class MenuItemBase extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //SKU = prefix-category-id
    private String sku;

    @NotBlank(message = "name cannot be blank")
    private String name;
    private String category;

    private String imageUrl;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private boolean active = true;

    @Override
    protected void handleAfterPersist() {
        if (this.sku == null || this.sku.isBlank()) {
            String prefix = (this instanceof Dish) ? "DISH" : "COMBO";
            String cat = (this.category == null || this.category.isBlank())
                    ? "GEN"
                    : this.category.toUpperCase().replaceAll("\\s+", "_");
            this.sku = prefix + "-" + cat + "-" + this.id;
        }
    }

}
