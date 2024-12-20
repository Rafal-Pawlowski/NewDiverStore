package com.ralf.NewDiverStore.order.controller;

import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.domain.repository.CustomerRepository;
import com.ralf.NewDiverStore.customer.service.CustomerService;
import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.service.OrderService;
import com.ralf.NewDiverStore.order.service.SessionOrderService;
import com.ralf.NewDiverStore.wrapper.AddressWrapper;
import jakarta.servlet.http.HttpSession;
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
@SessionAttributes({"customer", "addressWrapper"})
public class OrderViewController {

    private static final Logger logger = LoggerFactory.getLogger(OrderViewController.class);
    private final SessionOrderService sessionOrderService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final SessionCartService sessionCartService;


    public OrderViewController(SessionOrderService sessionOrderService, OrderService orderService, CustomerService customerService, CustomerRepository customerRepository, SessionCartService sessionCartService) {
        this.sessionOrderService = sessionOrderService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.sessionCartService = sessionCartService;
    }

    @GetMapping("/customer-details")
    public String customerDetailsView(Model model) {
        model.addAttribute("step", "details");

        Order order = sessionOrderService.initiateOrderSession();
        logger.debug("Session order initiated");

        model.addAttribute("customer", new Customer());

        return "order/customer-details";
    }

    @PostMapping("/customer-details")
    public String customerDetailsFormEdit(
            @Valid @ModelAttribute("customer") Customer customerForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            logger.error("Error while completing form");
            model.addAttribute("message", Message.error("Data writing error"));
            return "order/customer-details";
        }

        try {
            Order order = sessionOrderService.getOrder();
            Customer savedCustomer = customerService.createOrUpdateCustomerWithNamesAndEmail(customerForm);
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
        model.addAttribute("step", "shipping");
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

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors detected: {}", bindingResult.getAllErrors());
            model.addAttribute("message", Message.error("Data writing error"));
            return "order/address";
        }

        try {
            customer.setShippingAddress(addressWrapper.getShippingAddress());
            customer.setBillingAddress(addressWrapper.getBillingAddress());

            return "redirect:/order/payment";

        } catch (Exception e) {
            logger.error("Exception shown", e);
            model.addAttribute("message", Message.error("Unknown data writing error. Adding order failed."));
            return "order/address";
        }
    }


    @GetMapping("/payment")
    public String paymentView(Model model) {
        Order order = sessionOrderService.getOrder();
        model.addAttribute("order", order);
        model.addAttribute("step", "payment"); // do dynamicznej wizualizacji postępu zamówienia

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

            customer.addOrder(order);
            customerRepository.save(customer);

            orderService.createOrder(order);

            return "redirect:/order/greetings/" + order.getId();

        } catch (Exception e) {
            logger.error("Exception shown", e);
            model.addAttribute("message", Message.error("Unknown data writing error. Adding order failed."));
            return "order/address";
        }

    }


    @GetMapping("/greetings/{order-id}")
    public String greetingsView(@PathVariable("order-id") UUID orderId,
                                @ModelAttribute("customer") Customer customer,
                                Model model, HttpSession session) {

        Order order = orderService.findOrder(orderId);

        model.addAttribute("order", order);
        model.addAttribute("customer", customer);
        model.addAttribute("step", "summary");

        sessionCartService.clearCart();
        session.removeAttribute("scopedTarget.sessionCartService");

        return "order/greetings";
    }


}
