package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.dto.DashboardDto;
import com.theplutushome.optimus.dto.PaymentOrderDto;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.service.OrdersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/optimus/v1/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // @GetMapping("/list/orders/{email}")
    public List<PaymentOrderDto> getOrders(@PathVariable(value = "email") String email,
            @RequestHeader("Authorization") String authHeader) {
        List<PaymentOrderDto> orders = ordersService.getAllOrders(email, authHeader);
        if (orders != null) {
            return orders.stream()
                    .sorted((o1, o2) -> o2.createdAt().compareTo(o1.createdAt())) // Sort by createdAt in descending
                                                                                  // order
                    .collect(Collectors.toList());
        }
        return null;
    }


    @GetMapping("/find/order/{orderId}")
    public ResponseEntity<PaymentOrder> getOrder(@PathVariable int orderId) {
        PaymentOrder order = ordersService.findOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/find/all")
    public List<PaymentOrderDto> getAllOrders() {
        return ordersService.getAllOrders().stream()
                .sorted((o1, o2) -> o2.createdAt().compareTo(o1.createdAt()))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/dashboard/data")
    public DashboardDto getDashboardData() {
        return ordersService.getDashboardData();
    }

}
