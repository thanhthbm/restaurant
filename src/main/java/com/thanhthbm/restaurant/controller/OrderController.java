package com.thanhthbm.restaurant.controller;

import com.thanhthbm.restaurant.domain.request.ReqCreateOrderDTO;
import com.thanhthbm.restaurant.domain.response.ResCreateOrderDTO;
import com.thanhthbm.restaurant.service.OrderService;
import com.thanhthbm.restaurant.util.annotation.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @ApiMessage("Create an order")
    public ResponseEntity<ResCreateOrderDTO> createOrder(@RequestBody ReqCreateOrderDTO req) {
        return ResponseEntity.ok(orderService.createOrder(req));
    }
}
