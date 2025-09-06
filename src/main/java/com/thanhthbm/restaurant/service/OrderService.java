package com.thanhthbm.restaurant.service;

import com.thanhthbm.restaurant.domain.MenuItemBase;
import com.thanhthbm.restaurant.domain.Order;
import com.thanhthbm.restaurant.domain.OrderItem;
import com.thanhthbm.restaurant.domain.Table;
import com.thanhthbm.restaurant.domain.request.ReqCreateOrderDTO;
import com.thanhthbm.restaurant.domain.response.ResCreateOrderDTO;
import com.thanhthbm.restaurant.repository.MenuItemBaseRepository;
import com.thanhthbm.restaurant.repository.OrderRepository;
import com.thanhthbm.restaurant.repository.TableRepository;
import com.thanhthbm.restaurant.service.mapper.OrderMapper;
import com.thanhthbm.restaurant.util.constant.OrderStatus;
import com.thanhthbm.restaurant.util.constant.OrderType;
import com.thanhthbm.restaurant.util.constant.TableStatus;
import com.thanhthbm.restaurant.util.exception.ResourceNotFoundException;
import com.thanhthbm.restaurant.util.exception.TableNotAvailableException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final MenuItemBaseRepository menuItemBaseRepository;
    private final MenuService menuService;
    private final TableService tableService;


    public OrderService(
            OrderRepository orderRepository,
            TableRepository tableRepository,
            MenuItemBaseRepository menuItemBaseRepository,
            MenuService menuService,
            TableService tableService

    ) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.menuItemBaseRepository = menuItemBaseRepository;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public ResCreateOrderDTO createOrder(ReqCreateOrderDTO req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least 1 item");
        }

        Order order = new Order();
        order.setNote(req.getNote());
        order.setOrderStatus(OrderStatus.OPEN);
        order.setOrderType(req.getOrderType());

        // ---- Build items (snapshot giá + subtotal) ----
        List<OrderItem> items = new ArrayList<>();
        for (ReqCreateOrderDTO.ItemOrderDTO it : req.getItems()) {
            MenuItemBase menuItem = menuItemBaseRepository.findById(it.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + it.getItemId()));

            int qty = it.getQuantity();
            if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0");

            BigDecimal unitPrice = menuService.getCurrentPrice(menuItem);
            BigDecimal lineSubtotal = unitPrice.multiply(BigDecimal.valueOf(qty));

            OrderItem oi = new OrderItem();
            oi.setOrder(order);                // quan hệ 2 chiều
            oi.setOrderedDish(menuItem);       // hoặc orderedItem
            oi.setPrice(unitPrice);        // >> đổi tên field price -> unitPrice
            oi.setQuantity(qty);
            oi.setSubTotal(lineSubtotal);
            items.add(oi);
        }
        order.setOrderItems(items);

        // ---- Bàn (chỉ khi không TAKE_AWAY) ----
        if (req.getOrderType() != OrderType.TAKE_AWAY) {
            if (req.getTableIds() == null || req.getTableIds().isEmpty()) {
                throw new IllegalArgumentException("Table(s) required for non TAKE_AWAY order");
            }

            // tải đủ bàn, nếu thiếu bàn nào -> throw
            List<Table> tables = tableRepository.findAllById(req.getTableIds());
            if (tables.size() != req.getTableIds().size()) {
                throw new ResourceNotFoundException("Some table ids not found");
            }

            for (Table t : tables) {
                if (t.getStatus() != TableStatus.AVAILABLE) {
                    throw new TableNotAvailableException("Table not available: " + t.getId());
                }
                tableService.updateTableStatus(t, TableStatus.OCCUPIED); // hoặc RESERVED
            }
            order.setTables(tables);
        } else {
            order.setTables(Collections.emptyList());
        }

        // ---- Tính tổng ----
        BigDecimal total = items.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        order.setNote(req.getNote());

        // ---- Lưu 1 lần ----
        return OrderMapper.mapOrderToDTO(orderRepository.save(order));
    }


}
