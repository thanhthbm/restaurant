package com.thanhthbm.restaurant.domain;


import com.thanhthbm.restaurant.util.constant.PriceMode;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "combos")
@DiscriminatorValue("COMBO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Combo extends MenuItemBase{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriceMode priceMode = PriceMode.SUM;

    private BigDecimal price;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String note;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComboItem> items = new ArrayList<>();

    public void setItems(List<ComboItem> items) {
        this.items.clear();

        if (items != null) {
            this.items.addAll(items);
        }
    }

    public void addItem(ComboItem item) {
        item.setCombo(this);
        this.items.add(item);
    }

    @Override
    protected void handleBeforePersist() { syncPrice(); }

    @Override
    protected void handleBeforeUpdate() { syncPrice(); }

    private void syncPrice() {
        if (priceMode == PriceMode.SUM) {
            this.price = items.stream()
                    .map(it -> it.getDish().getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else if (this.price == null) {
            throw new IllegalStateException("priceMode=FIXED thì 'price' không được null");
        }
    }


}
