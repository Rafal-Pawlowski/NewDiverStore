package com.ralf.NewDiverStore.order.controller;

import com.ralf.NewDiverStore.AddressWrapper;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.customer.domain.model.Customer;
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
@SessionAttributes({"customer", "addressWrapper", "sameAddress"})
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
            Customer savedCustomer = customerService.createOrUpdateCustomerWithNamesAndEmail(customerForm);
            logger.debug("Created or updated customer to DB: {}", savedCustomer);

            redirectAttributes.addFlashAttribute("message", Message.info("New Order added"));

            return "redirect:/order/address-form";

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

        model.addAttribute("addressWrapper", new AddressWrapper());


        return "order/address";
    }

    @PostMapping("/address-form")
    public String addressFormEditView(
            @ModelAttribute("addressWrapper") AddressWrapper addressWrapper,
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

            logger.debug("Got shippingAddres from shippingAddressForm: {}", addressWrapper.getShippingAddress());


            logger.debug("ShippingAddressForm.getStreet: {}", addressWrapper.getShippingAddress().getStreet());


            //przypisanie adresów do customera
            customer.setShippingAddress(addressWrapper.getShippingAddress());
            logger.debug("After setting shippingAddres in Customer: {}", customer.getShippingAddress().getId());
            logger.debug("Before copying same billingAddress: {}", customer.isSameAddress());
            if (customer.isSameAddress()) {
                addressWrapper.getBillingAddress().sameAddressUpdate(customer.getShippingAddress());
            }

            logger.debug("After copying same billingAddress: {}", customer.getBillingAddress());
            logger.debug("Customer Before setting billingAddress: {}", customer.getBillingAddress());
            customer.setBillingAddress(addressWrapper.getBillingAddress());
            logger.debug("Customer After setting billingAddress: {}", customer.getBillingAddress());

            return "redirect:/order/payment";

        } catch (Exception e) {
            logger.error("Exception shown", e);
            model.addAttribute("message", Message.error("Unknown data writing error. Adding order failed."));
            return "order/address";
        }
    }

    //TODO PAYMENT VIEW

    @GetMapping("/payment")
    public String paymentView(Model model) {
        Order order = sessionOrderService.getOrder();
        model.addAttribute("order", order);

        return "order/payment";
    }

    @PostMapping("/payment")
    public String paymentEditAndFinalOrderOperations(@ModelAttribute("order") Order order,
                                                     @ModelAttribute("customer") Customer customer,
                                                     BindingResult bindingResult,
                                                     RedirectAttributes redirectAttributes,
                                                     Model model) {
        if (bindingResult.hasErrors()) {
            logger.error("Error while completing form");
            model.addAttribute("message", Message.error("Data writing error"));
            return "order/payment";
        }

        try {
            logger.debug("Order Payment: {}", order.getPayment());

            customer.addOrder(order);
            logger.debug("Customer before persisting entity: {}", customer);
            customerRepository.save(customer);
            logger.debug("Customer After persisting entity: {}", customer);

            logger.debug("Order before persisting entity: {}", order);

            orderService.createOrder(order);
            logger.debug("Order After persisting entity: {}", order);


            return "redirect:/order/greetings/" + order.getId();

        } catch (
                Exception e) {
            logger.error("Exception shown", e);
            model.addAttribute("message", Message.error("Unknown data writing error. Adding order failed."));
            return "order/address";
        }

    }


    @GetMapping("/greetings/{order-id}")
    public String greetingsView(@PathVariable("order-id") UUID orderId, @ModelAttribute("customer") Customer customer, Model model) {
        //Rozwiazanie GPT
        logger.info("wyświetlenie widoku Greetings");
        Order order = orderService.findOrder(orderId);
        logger.debug("GREETINGSVIEW - Payment: {}", order.getPayment());
        model.addAttribute("order", order);
        model.addAttribute("customer", customer);


        return "order/greetings";
    }


}
