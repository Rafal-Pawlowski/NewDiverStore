package com.ralf.NewDiverStore.order.controller;

import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.common.dto.Message;
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


    public OrderViewController(SessionOrderService sessionOrderService, OrderService orderService) {
        this.sessionOrderService = sessionOrderService;

        this.orderService = orderService;
    }

    @GetMapping("/shipping")
    public String shippingFormView(Model model){
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
    ){
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", Message.error("Data writing error"));
            return "order/shipping";
        }

        try{
            //needs to get order from session
            Order order = sessionOrderService.getOrder();

            order.setCustomer(orderForm.getCustomer());
            order.setPayment(orderForm.getPayment());
// Czy tu wystąpi błąd JPA? nie znajdzie w bazie danych o podanym ID trzeba bedzie zmienić metode create.
            orderService.updateOrder(order.getId() ,order);
            redirectAttributes.addFlashAttribute("message", Message.info("New Order added"));
        } catch (Exception e){
            model.addAttribute("message", Message.error("Unknown data writing error. Adding order failed."));
            return "order/shipping";
        }
        return "redirect:/order/greetings";

    }


    @GetMapping("/greetings/{order-id}")
    public String greetingsView(@PathVariable("order-id")UUID orderId, Model model){
        model.addAttribute("order", orderService.findOrder(orderId));
        return "order/greetings";
    }

}
