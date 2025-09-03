package com.thanhthbm.restaurant.domain.request;

import com.thanhthbm.restaurant.util.constant.OrderType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ReqCreateOrderDTO {
    @NotNull(message = "Order type must not be null")
    private OrderType orderType;

    private List<Long> tableIds;

    @NotNull(message = "Items must not be null")
    @Size(min = 1, message = "Order must contain at least one item")
    private List<@Valid ItemOrderDTO> items;

    private String note;

    private Long userId;

    private Long customerId;

    @Data
    public static class ItemOrderDTO{
        @NotNull(message = "Item id must not be null")
        private Long itemId;
        @Min(value = 1, message = "Quantity must be at least 1")
        private int quantity;
    }
}
