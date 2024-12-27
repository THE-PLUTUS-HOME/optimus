package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.dto.PaymentOrderDto;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/optimus/v1/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/list/{email}")
    public List<PaymentOrderDto> getOrders(@PathVariable(value = "email") String email, @RequestHeader("Authorization") String authHeader) {
        return ordersService.getAllOrders(email, authHeader);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentOrder> getOrder(@RequestHeader("Authorization") String authHeader, @PathVariable int orderId) {
        PaymentOrder order = ordersService.findOrder(orderId, authHeader);
        return ResponseEntity.ok(order);
    }

}
