package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.entity.Order;
import com.theplutushome.optimus.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/optimus/api/v1/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/list/{userId}")
    public List<Order> getOrders(@PathVariable(value = "userId") int userId, @RequestHeader("Authorization") String authHeader) {
        return ordersService.getAllOrders(userId, authHeader);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@RequestHeader("Authorization") String authHeader, @PathVariable int orderId) {
        Order order = ordersService.findOrder(orderId, authHeader);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestBody @Valid Order order, @RequestHeader("Authorization") String authHeader) {
        ordersService.createOrder(order, authHeader);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order created");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}
