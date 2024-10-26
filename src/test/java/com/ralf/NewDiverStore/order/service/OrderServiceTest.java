package com.ralf.NewDiverStore.order.service;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.domain.repository.CustomerRepository;
import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.domain.model.Payment;
import com.ralf.NewDiverStore.order.domain.repository.OrderRepository;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import com.ralf.NewDiverStore.product.service.RedisProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@Rollback
class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private SessionCartService sessionCartService;

    @MockBean
    private RedisProductService redisProductService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    void shouldCreateOrder() {
        //given
        Cart cart = new Cart();
        Product product = productRepository.save( new Product("Product", new BigDecimal("123.00")));
        cart.addItem(product);
        cart.addItem(product);

        when(sessionCartService.getCart()).thenReturn(cart);

        Order orderRequest = new Order();
        Customer customer = customerRepository.save(new Customer());
        orderRequest.setCustomer(customer);
        orderRequest.setPayment(Payment.CREDIT_CARD);

//        Order savedOrder = new Order();
//        savedOrder.setId(UUID.randomUUID());
//        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        doNothing().when(redisProductService).setOrderCount(any(UUID.class), anyLong());

        //when
        Order result = orderService.createOrder(orderRequest);

        //then
        assertThat(result).isNotNull();
        verify(redisProductService).setOrderCount(product.getId(), 2L);

        assertThat(result.getTotalOrderPrice()).isEqualTo(cart.getTotalCost());
        assertThat(result.getShippingCost()).isEqualTo(cart.getShipping().calculateShippingCost(cart.getSum()));
        assertThat(result.getCustomer()).isEqualTo(orderRequest.getCustomer());
        assertThat(result.getOrderItems()).hasSize(1);
    }

    @Test
    void findOrders() {
    }

    @Test
    void findOrder() {
    }

    @Test
    void updateOrder() {
    }

    @Test
    void deleteOrder() {
    }
}