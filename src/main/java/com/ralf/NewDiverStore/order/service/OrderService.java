package com.ralf.NewDiverStore.order.service;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.domain.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createNewOrder() {
        Order order = new Order();
        logger.debug("New Order added: {}", order);

        LocalDateTime orderDateTime = LocalDateTime.now();
        order.setOrderTime(orderDateTime);

        logger.debug("Order time: {}", orderDateTime);
        return orderRepository.save(order);

    }

    @Transactional
    public Order createOrder(Order orderRequest) {
        Order order = new Order();
        logger.debug("New Order added with id: {}", order.getId());
        order.setPayment(orderRequest.getPayment());
        logger.debug("Payment set to: {}", order.getPayment());

        Customer customer = orderRequest.getCustomer();
        customer.addOrder(order);
        logger.debug("Customer to Order added: {}", order.getCustomer().getId());

        LocalDateTime orderDateTime = LocalDateTime.now();
        order.setOrderTime(orderDateTime);
        logger.debug("Order time: {}", orderDateTime);

        logger.debug("Zamówienie przed zapisem: {}", order);
        logger.debug("Zamówienie przed zapisem, id klienta: {}", order.getCustomer().getId());
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
