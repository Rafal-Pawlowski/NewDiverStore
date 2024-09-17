package com.ralf.NewDiverStore.order.controller;

import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.customer.domain.model.BillingAddress;
import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.domain.model.CustomerBuilder;
import com.ralf.NewDiverStore.customer.domain.model.ShippingAddress;
import com.ralf.NewDiverStore.customer.domain.repository.CustomerRepository;
import com.ralf.NewDiverStore.customer.service.CustomerService;
import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.service.OrderService;
import com.ralf.NewDiverStore.order.service.SessionOrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/order")
@SessionAttributes("customer")
public class OrderViewController {


    private static final Logger logger = LoggerFactory.getLogger(OrderViewController.class);
    private final SessionOrderService sessionOrderService;

    private final OrderService orderService;

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;


    public OrderViewController(SessionOrderService sessionOrderService, OrderService orderService, CustomerService customerService, CustomerRepository customerRepository) {
        this.sessionOrderService = sessionOrderService;

        this.orderService = orderService;
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customer-details")
    public String customerDetailsView(Model model) {
        logger.info(" wyswietlenie GetMapping /customer-details");
        Order order = sessionOrderService.initiateOrderSession();
        logger.debug("Session order initiated");


        model.addAttribute("customer", new Customer());


        logger.debug("order customer: {}", order.getCustomer());
;

        logger.debug("New customer added");

        return "order/customer-details";
    }

    @PostMapping("/customer-details")
    public String customerDetailsFormEdit(
            @Valid @ModelAttribute("customer") Customer customerForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        logger.debug("PostMapping : /customer-details");

        if (bindingResult.hasErrors()) {
            logger.error("Error while completing form");
            model.addAttribute("message", Message.error("Data writing error"));
            return "order/customer-details";
        }

        try {
            Order order = sessionOrderService.getOrder();
            logger.debug("Got order from session: {}", order);


            logger.debug("Got customer from orderForm: {}", customerForm);
            Customer savedCustomer = customerService.createOrUpdateCustomer(customerForm);
            logger.debug("Created or updated customer to DB: {}", savedCustomer);

//            customerForm.addOrder(order);   //setCustomer and addOrder
//            logger.info("Added order to customer, customer connected to order: {}", customerForm );


            logger.debug("Before creating order: {}", order);
            orderService.createOrder(order);
            logger.info("After New order created in DB: {}", order);

            redirectAttributes.addFlashAttribute("message", Message.info("New Order added"));

            return "redirect:/order/greetings/" + order.getId();

        } catch (Exception e) {
            logger.error("Exception shown", e);
            model.addAttribute("message", Message.error("Unknown data writing error. Adding order failed."));
            return "order/customer-details";
        }
    }


    @GetMapping("/greetings/{order-id}")
    public String greetingsView(@PathVariable("order-id") UUID orderId, Model model) {
        logger.info("wyświetlenie widoku Greetings");
        model.addAttribute("order", orderService.findOrder(orderId));
        return "order/greetings";
    }

}
