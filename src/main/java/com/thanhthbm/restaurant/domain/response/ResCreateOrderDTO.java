package com.thanhthbm.restaurant.domain.response;

import com.thanhthbm.restaurant.util.constant.OrderStatus;
import com.thanhthbm.restaurant.util.constant.OrderType;
import com.thanhthbm.restaurant.util.constant.TableStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ResCreateOrderDTO {
    private long orderId;

    private StaffOrder staff;

    private OrderStatus orderStatus;

    private OrderType orderType;
    private CustomerOrder customer;

    private BigDecimal total;

    @NotNull(message = "Items must not be null")
    @Size(min = 1, message = "Order must contain at least one item")
    private List<ItemOrder> itemOrders;

    private List<TableOrder> tableOrders;

    @Builder
    @Getter
    @Setter
    public static class StaffOrder{
        private long staffId;
        private String staffName;
    }

    @Builder
    @Getter
    @Setter
    public static class CustomerOrder{
        private long customerId;
        private String customerName;
    }

    @Getter
    @Setter
    @Builder
    public static class ItemOrder{
        private long itemId;
        private String itemName;
        private BigDecimal price;
        private int quantity;
        private BigDecimal subtotal;
    }

    @Getter
    @Setter
    @Builder
    public static class TableOrder{
        private long tableId;
        private String tableName;
        private TableStatus tableStatus;
    }


}
