package com.ralf.NewDiverStore.order.controller;

import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.customer.domain.model.BillingAddress;
import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.domain.model.CustomerBuilder;
import com.ralf.NewDiverStore.customer.domain.model.ShippingAddress;
import com.ralf.NewDiverStore.customer.service.CustomerService;
import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.service.OrderService;
import com.ralf.NewDiverStore.order.service.SessionOrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderViewController {

    private final SessionOrderService sessionOrderService;

    private final OrderService orderService;

    private final CustomerService customerService;


    public OrderViewController(SessionOrderService sessionOrderService, OrderService orderService, CustomerService customerService) {
        this.sessionOrderService = sessionOrderService;

        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/shipping")
    public String shippingFormView(Model model) {
        Order order = sessionOrderService.initiateOrderSession();

        model.addAttribute("order", order);

        return "order/shipping";
    }

    @PostMapping("/shipping")
    public String shippingFormEdit(
            @Valid @ModelAttribute("order") Order orderForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", Message.error("Data writing error"));
            return "order/shipping";
        }

        try {
            //needs to get order from session
            Order order = sessionOrderService.getOrder();
            Customer customer = orderForm.getCustomer();
            customerService.createCustomer(customer);
            order.setCustomer(customer);
            order.setPayment(orderForm.getPayment());

            if (order.getCustomer().isSameAddress()) {
                BillingAddress billingAddress = order.getCustomer().getBillingAddress();
                ShippingAddress shippingAddress = order.getCustomer().getShippingAddress();
                billingAddress.copyFrom(shippingAddress);

            }

            customerService.updateCustomer(customer.getId(), customer);
// Czy tu wystąpi błąd JPA? nie znajdzie w bazie danych o podanym ID trzeba bedzie zmienić metode create.
            orderService.createOrder(order);
            redirectAttributes.addFlashAttribute("message", Message.info("New Order added"));

            return "redirect:/order/greetings/" + order.getId();

        } catch (Exception e) {
            model.addAttribute("message", Message.error("Unknown data writing error. Adding order failed."));
            return "order/shipping";
        }
    }


    @GetMapping("/greetings/{order-id}")
    public String greetingsView(@PathVariable("order-id") UUID orderId, Model model) {
        model.addAttribute("order", orderService.findOrder(orderId));
        return "order/greetings";
    }

}
