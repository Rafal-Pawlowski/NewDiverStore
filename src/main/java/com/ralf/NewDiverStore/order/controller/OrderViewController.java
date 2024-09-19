package com.ralf.NewDiverStore.order.controller;

import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.customer.domain.model.*;
import com.ralf.NewDiverStore.customer.domain.repository.CustomerRepository;
import com.ralf.NewDiverStore.customer.service.CustomerService;
import com.ralf.NewDiverStore.order.domain.model.Order;
//import com.ralf.NewDiverStore.order.domain.model.Payment;
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
@SessionAttributes({"customer", "BillingAddress", "ShippingAddress", "sameAddress"})
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
//            orderService.createOrder(order);
            logger.info("After New order created in DB: {}", order);

            redirectAttributes.addFlashAttribute("message", Message.info("New Order added"));

            return "redirect:/order/address-form";
//            return "redirect:/order/greetings/" + order.getId();

        } catch (Exception e) {
            logger.error("Exception shown", e);
            model.addAttribute("message", Message.error("Unknown data writing error. Adding order failed."));
            return "order/customer-details";
        }
    }


    @GetMapping("/address-form")
    public String addressFormView(
            @ModelAttribute("customer") Customer customer,
            Model model) {
        logger.info("wyświetlenie formularza address-form");
        Order order = sessionOrderService.getOrder();
        model.addAttribute("shippingAddress", new ShippingAddress());
        model.addAttribute("billingAddress", new BillingAddress());
        model.addAttribute("order", order);


        return "order/address";
    }

    @PostMapping("/address-form")
    public String addressFormEditView(
            @ModelAttribute("shippingAddress") ShippingAddress shippingAddressForm,
            @ModelAttribute("billingAddress") BillingAddress billingAddressForm,
            @ModelAttribute("customer") Customer customer,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        logger.debug("PostMapping : /address");

        if (bindingResult.hasErrors()) {
            logger.error("Error while completing form");
            model.addAttribute("message", Message.error("Data writing error"));
            return "order/address";
        }

        try {
            Order order = sessionOrderService.getOrder();
            logger.debug("Getting Order from session. Order id: {}", order.getId());

            logger.debug("Got shippingAddres from shippingAddressForm: {}", shippingAddressForm);


            //settery parametrów adresów
            logger.debug("ShippingAddressForm.getStreet: {}", shippingAddressForm.getStreet());

            //metoda if sameAddress?

            //przypisanie adresów do customera
            customer.setShippingAddress(shippingAddressForm);
            logger.debug("After setting shippingAddres in Customer: {}", shippingAddressForm.getId());
            logger.debug("Before copying same billingAddress: {}", customer.isSameAddress());
            if (customer.isSameAddress()) {
                billingAddressForm.setStreet(shippingAddressForm.getStreet());
                billingAddressForm.setCountry(shippingAddressForm.getCountry());
                billingAddressForm.setCity(shippingAddressForm.getCity());
                billingAddressForm.setZip(shippingAddressForm.getZip());
            }
            logger.debug("After copying same billingAddress: {}", customer.getBillingAddress());
            logger.debug("Customer Before setting billingAddress: {}", customer.getBillingAddress());
            customer.setBillingAddress(billingAddressForm);
            logger.debug("Customer After setting billingAddress: {}", customer.getBillingAddress());

//            logger.debug("Order Payment: {}", order.getPayment());
            //update Customera do BD
customer.addOrder(order);
            logger.debug("Customer before persisting entity: {}", customer);
            customerRepository.save(customer);
            logger.debug("Customer After persisting entity: {}", customer);

            logger.debug("Order before persisting entity: {}", order);

            orderService.createOrder(order);
            logger.debug("Order After persisting entity: {}", order);

            return "redirect:/order/greetings/" + order.getId();

        } catch (Exception e) {
            logger.error("Exception shown", e);
            model.addAttribute("message", Message.error("Unknown data writing error. Adding order failed."));
            return "order/address";
        }

    }


    @GetMapping("/greetings/{order-id}")
    public String greetingsView(@PathVariable("order-id") UUID orderId, Model model) {
        logger.info("wyświetlenie widoku Greetings");
        model.addAttribute("order", sessionOrderService.getOrder());
        return "order/greetings";
    }


}
