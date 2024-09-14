package com.ralf.NewDiverStore.order.controller;

import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/orders")
public class OrderApiController {

    private final OrderService orderService;


    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody Order orderRequest){
        return orderService.createOrder(orderRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Page<Order> getOrders(Pageable pageable){
        return orderService.findOrders(pageable);
    }

    @GetMapping("{order-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Order getOrder(@PathVariable("order-id")UUID orderId){
        return orderService.findOrder(orderId);
    }

    @PutMapping("{order-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Order updateOrder(@PathVariable("order-id")UUID orderId, @RequestBody Order orderRequest){
        return orderService.updateOrder(orderId, orderRequest);
    }



    @DeleteMapping("{order-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("order-id")UUID orderId){
        orderService.deleteOrder(orderId);
    }
}
