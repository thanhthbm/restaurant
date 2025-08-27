package com.thanhthbm.restaurant.domain.request;

import com.thanhthbm.restaurant.util.constant.PriceMode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateComboDTO {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "category cannot be blank")
    private String category;
    private String description;

    private String note;

    @NotNull(message = "price mode is required")
    @Enumerated(EnumType.STRING)
    private PriceMode priceMode;

    //required if priceMode is Fixed
    private BigDecimal price;

    @Size(min = 1, message = "Combo must have at least one item")
    private List<@Valid DishCombo> items;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DishCombo{
        private long id;
        private int quantity;
    }
}
