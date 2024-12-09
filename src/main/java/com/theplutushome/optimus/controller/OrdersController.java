package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/optimus/api/v1/orders")
public class OrdersController {

    private final OrdersService ordersService;
    
    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }
}
