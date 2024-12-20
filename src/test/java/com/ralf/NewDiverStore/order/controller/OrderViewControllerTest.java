package com.ralf.NewDiverStore.order.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.customer.domain.model.BillingAddress;
import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.domain.model.ShippingAddress;
import com.ralf.NewDiverStore.customer.domain.repository.CustomerRepository;
import com.ralf.NewDiverStore.customer.service.CustomerService;
import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.service.OrderService;
import com.ralf.NewDiverStore.order.service.SessionOrderService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.wrapper.AddressWrapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class OrderViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionOrderService sessionOrderService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private SessionCartService sessionCartService;

    @MockBean
    private BindingResult bindingResult;

    private Customer mockCustomer;

    private Order mockOrder;

    @BeforeEach
    void setUp() {
        Cart mockCart = new Cart();
        Product product = new Product("Product", new BigDecimal("400.00"));
        mockCart.addItem(product);
        when(sessionCartService.getCart()).thenReturn(mockCart);

        mockCustomer = new Customer("John", "Doe", "john@example.com");
        mockOrder = new Order();
        when(sessionOrderService.initiateOrderSession()).thenReturn(mockOrder);
        when(sessionOrderService.getOrder()).thenReturn(mockOrder);
        when(customerService.createOrUpdateCustomerWithNamesAndEmail(any(Customer.class))).thenReturn(mockCustomer);
        when(orderService.findOrder(any(UUID.class))).thenReturn(mockOrder);
        Mockito.reset(bindingResult);
    }

    @Test
    void shouldDisplayCustomerDetailsView() throws Exception {
        mockMvc.perform(get("/order/customer-details"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/customer-details"))
                .andExpect(model().attribute("step", "details"))
                .andExpect(model().attributeExists("customer"));

    }

    @Test
    void shouldProcessCustomerDetailsFormAndRedirect() throws Exception {

        mockMvc.perform(post("/order/customer-details")
                        .flashAttr("customer", mockCustomer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/address-form"))
                .andExpect(flash().attributeExists("message"));

        verify(customerService).createOrUpdateCustomerWithNamesAndEmail(mockCustomer);
        verify(sessionOrderService).getOrder();
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void shouldReturnToCustomerDetailsViewWhenValidationFails() throws Exception {

        Customer invalidCustomer = new Customer();
        invalidCustomer.setFirstName("invalid");

        mockMvc.perform(post("/order/customer-details")
                        .flashAttr("customer", invalidCustomer))
                .andExpect(status().isOk())
                .andExpect(view().name("order/customer-details"))
                .andExpect(model().attributeExists("message"));

        verify(customerService, never()).createOrUpdateCustomerWithNamesAndEmail(any());
    }


    @Test
    void shouldDisplayAddressFormView() throws Exception {
        // Przygotowanie danych testowych
        Customer mockCustomer = new Customer();
        mockCustomer.setFirstName("John");
        mockCustomer.setLastName("Doe");

        // Wywołanie endpointa z mockowanym customerem
        mockMvc.perform(get("/order/address-form")
                        .flashAttr("customer", mockCustomer))
                .andExpect(status().isOk())
                .andExpect(view().name("order/address"))
                .andExpect(model().attributeExists("step", "addressWrapper"))
                .andExpect(model().attribute("step", "shipping"))
                .andExpect(model().attribute("addressWrapper", instanceOf(AddressWrapper.class)));
    }


    @Test
    void shouldProcessAddressformAndRedirectToPayment() throws Exception {

        AddressWrapper addressWrapper = new AddressWrapper();
        ShippingAddress shippingAddress = new ShippingAddress("ShippingStreet", "ShippingCountry", "ShippingCity", "00-001");
        BillingAddress billingAddress = new BillingAddress("BillingStreet", "BillingCountry", "BillingCity", "00-002");
        addressWrapper.setShippingAddress(shippingAddress);
        addressWrapper.setBillingAddress(billingAddress);


        mockMvc.perform(post("/order/address-form")
                        .flashAttr("addressWrapper", addressWrapper)
                        .flashAttr("customer", mockCustomer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/payment"));

        assertThat(mockCustomer.getShippingAddress()).isEqualTo(shippingAddress);
        assertThat(mockCustomer.getBillingAddress()).isEqualTo(billingAddress);

    }


    @Test
    void shouldDisplayPaymentView() throws Exception {

        mockMvc.perform(get("/order/payment"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payment"))
                .andExpect(model().attribute("step", "payment"))
                .andExpect(model().attribute("order", mockOrder));
    }

    @Test
    void shouldProcessPaymentAndRedirectToGreetings() throws Exception {

        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);
        when(orderService.createOrder(any(Order.class))).thenReturn(mockOrder);

        mockMvc.perform(post("/order/payment")
                        .flashAttr("order", mockOrder)
                        .flashAttr("customer", mockCustomer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/greetings/" + mockOrder.getId()));
    }

    @Test
    void shouldDisplayGreetingsView() throws Exception {
        ShippingAddress shippingAddress = new ShippingAddress("ShippingStreet", "ShippingCountry", "ShippingCity", "00-001");
        BillingAddress billingAddress = new BillingAddress("BillingStreet", "BillingCountry", "BillingCity", "00-002");
        mockCustomer.setShippingAddress(shippingAddress);
        mockCustomer.setBillingAddress(billingAddress);

        mockMvc.perform(get("/order/greetings/{order-id}", mockOrder.getId())
                        .flashAttr("customer", mockCustomer))
                .andExpect(status().isOk())
                .andExpect(view().name("order/greetings"))
                .andExpect(model().attribute("order", mockOrder))
                .andExpect(model().attribute("customer", mockCustomer))
                .andExpect(model().attribute("step", "summary"));

        verify(sessionCartService, times(1)).clearCart();
    }
}