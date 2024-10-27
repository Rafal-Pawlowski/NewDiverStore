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
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
        Product product = productRepository.save(new Product("Product", new BigDecimal("123.00")));
        cart.addItem(product);
        cart.addItem(product);

        when(sessionCartService.getCart()).thenReturn(cart);

        Order orderRequest = new Order();
        Customer customer = customerRepository.save(new Customer());
        orderRequest.setCustomer(customer);
        orderRequest.setPayment(Payment.CREDIT_CARD);

        doNothing().when(redisProductService).incrementOrderCount(any(UUID.class), anyLong());

        //when
        Order result = orderService.createOrder(orderRequest);

        //then
        assertThat(result).isNotNull();
        verify(redisProductService).incrementOrderCount(product.getId(), 2L);

        assertThat(result.getTotalOrderPrice()).isEqualTo(cart.getTotalCost());
        assertThat(result.getShippingCost()).isEqualTo(cart.getShipping().calculateShippingCost(cart.getSum()));
        assertThat(result.getCustomer()).isEqualTo(orderRequest.getCustomer());
        assertThat(result.getOrderItems()).hasSize(1);
    }

    @Test
    void shouldFindOrders() {
        //given
        Order order1 = orderRepository.save(new Order());
        Order order2 = orderRepository.save(new Order());
        Order order3 = orderRepository.save(new Order());

        //when
        Page<Order> result = orderService.findOrders(Pageable.unpaged());

        //then
        assertThat(result)
                .hasSize(3)
                .extracting(Order::getId)
                .containsExactly(order1.getId(), order2.getId(), order3.getId());
    }

    @Test
    void shouldFindOrder() {
        //given
        Order order = orderRepository.save(new Order());

        //when
        Order result = orderService.findOrder(order.getId());

        //then
        assertThat(result).isEqualTo(order);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhileFindingUnexistingOrder() {
        //given
        UUID id = UUID.randomUUID();

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.findOrder(id));

        assertThat(exception.getMessage()).isEqualTo("Order with id: " + id + " not found");
    }

    @Test
    void shouldUpdateOrder() {
        //given
        Order orderToUpdate = orderRepository.save(new Order());
        Customer customer = customerRepository.save(new Customer("Customer", "LastName", "email"));
        Payment payment = Payment.PAYPAL;
        Order orderRequest = new Order();
        orderRequest.setPayment(payment);
        orderRequest.setCustomer(customer);

        //when
        Order result = orderService.updateOrder(orderToUpdate.getId(), orderRequest);

        //then
        assertThat(result).isNotNull()
                .extracting(Order::getId, Order::getCustomer, Order::getPayment)
                .containsExactly(orderToUpdate.getId(), orderToUpdate.getCustomer(), orderToUpdate.getPayment());


    }

    @Test
    void shouldDeleteExistingOrder() {
        //given
        Order order = orderRepository.save(new Order());
        UUID orderId = order.getId();

        //when
        Throwable throwable = catchThrowable(() -> orderService.deleteOrder(orderId));

        //then
        assertThat(throwable).isNull();
        assertThat(orderRepository.findById(orderId)).isEmpty();
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhileRemovingNonExistingOrder(){
        //given
        UUID id = UUID.randomUUID();

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.deleteOrder(id));

        assertThat(exception.getMessage()).isEqualTo("Order with id: " + id + " not exist");

    }
}