package com.ralf.NewDiverStore.order.service;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.domain.model.CartItem;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.domain.model.OrderItem;
import com.ralf.NewDiverStore.order.domain.repository.OrderRepository;
import com.ralf.NewDiverStore.product.service.RedisProductService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    private final SessionCartService sessionCartService;

    private final RedisProductService redisProductService;


    public OrderService(OrderRepository orderRepository, SessionCartService sessionCartService, RedisTemplate<String, String> redisTemplate, RedisProductService redisProductService) {
        this.orderRepository = orderRepository;
        this.sessionCartService = sessionCartService;
        this.redisProductService = redisProductService;
    }


    @Transactional
    public Order createOrder(Order orderRequest) {

        Cart cart = sessionCartService.getCart();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem(
                    orderRequest,
                    cartItem.getProduct(),
                    cartItem.getCounter(),
                    cartItem.getProduct().getPrice()
            );
            orderRequest.addOrderItem(orderItem);

            //Redis
            redisProductService.incrementOrderCount(cartItem.getProduct().getId(), cartItem.getCounter());
        }

        BigDecimal totalCostNoShippingIncluded=cart.getSum();
        BigDecimal totalCostShippingIncluded = cart.getTotalCost();

        orderRequest.setTotalOrderPrice(totalCostShippingIncluded);
        orderRequest.setShippingCost(cart.getShipping().calculateShippingCost(totalCostNoShippingIncluded));

//        orderRequest.setPayment(orderRequest.getPayment()); // ???WT


        Customer customer = orderRequest.getCustomer();
        customer.addOrder(orderRequest);


        LocalDateTime orderDateTime = LocalDateTime.now();

        orderRequest.setOrderTime(orderDateTime);

        return orderRepository.save(orderRequest);
    }

    @Transactional(readOnly = true)
    public Page<Order> findOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Order findOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(()->
                new EntityNotFoundException("Order with id: " + orderId + " not found"));

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
        if(!orderRepository.existsById(orderId)){
            throw new EntityNotFoundException("Order with id: " + orderId + " not exist");
        }
        orderRepository.deleteById(orderId);
    }


}


