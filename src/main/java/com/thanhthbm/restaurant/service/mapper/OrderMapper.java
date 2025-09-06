package com.thanhthbm.restaurant.service.mapper;

import com.thanhthbm.restaurant.domain.Order;
import com.thanhthbm.restaurant.domain.OrderItem;
import com.thanhthbm.restaurant.domain.Table;
import com.thanhthbm.restaurant.domain.User;
import com.thanhthbm.restaurant.domain.response.ResCreateOrderDTO;

import java.util.List;
import java.util.Objects;

public final class OrderMapper {
    private OrderMapper() {}



    public static ResCreateOrderDTO mapOrderToDTO(Order order) {
        return ResCreateOrderDTO.builder()
                .orderId(order.getId())
                .staff(toStaff(order.getStaff()))
                .customer(toCustomer(order.getCustomer()))
                .orderStatus(order.getOrderStatus())
                .orderType(order.getOrderType())
                .total(order.getTotal())
                .itemOrders(toItems(order.getOrderItems()))
                .tableOrders(toTableOrders(order.getTables()))
                .build();
    }

    private static ResCreateOrderDTO.StaffOrder toStaff(User staff) {
        if (staff == null){
            return null;
        }
        return ResCreateOrderDTO.StaffOrder.builder()
                .staffId(staff.getId())
                .staffName(staff.getName())
                .build();
    }

    private static ResCreateOrderDTO.CustomerOrder toCustomer(User customer) {
        if (customer == null){
            return null;
        }

        return ResCreateOrderDTO.CustomerOrder.builder()
                .customerId(customer.getId())
                .customerName(customer.getName())
                .build();
    }

    private static List<ResCreateOrderDTO.ItemOrder> toItems(List<OrderItem> items) {
        if (items == null){
            return List.of();
        }

        return items.stream().filter(Objects::nonNull)
                .map(oi -> ResCreateOrderDTO.ItemOrder.builder()
                        .itemId(oi.getOrderedDish() != null ? oi.getOrderedDish().getId() : null)
                        .itemName(oi.getOrderedDish() != null ? oi.getOrderedDish().getName() : null)
                        .price(oi.getPrice())
                        .quantity(oi.getQuantity())
                        .subtotal(oi.getSubTotal())
                        .build()
                )
                .toList();
    }

    private static List<ResCreateOrderDTO.TableOrder> toTableOrders(List<Table> tables) {
        if (tables == null){
            return List.of();
        }

        return tables.stream().filter(Objects::nonNull)
                .map(t -> ResCreateOrderDTO.TableOrder.builder()
                        .tableId(t.getId())
                        .tableName(t.getName())
                        .tableStatus(t.getStatus())
                        .build()
                )
                .toList();
    }
}
