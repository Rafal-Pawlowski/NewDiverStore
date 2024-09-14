package com.ralf.NewDiverStore.order.service;

import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.domain.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Order orderRequest) {
        Order order = new Order();
        order.setPayment(orderRequest.getPayment());
        order.setCustomer(orderRequest.getCustomer());
        order.setOrderTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<Order> findOrders(Pageable pageable) {

        return orderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Order findOrder(UUID orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            return order;
        } else {
            throw new EntityNotFoundException("Order with id: " + orderId + " not found");
        }
    }

    @Transactional
    public Order updateOrder(UUID orderId, Order orderRequest) {
        Order order = findOrder(orderId);
        order.setCustomer(orderRequest.getCustomer());
        order.setPayment(orderRequest.getPayment());

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
