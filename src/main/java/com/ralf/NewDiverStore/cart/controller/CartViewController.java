package com.ralf.NewDiverStore.cart.controller;

import com.ralf.NewDiverStore.cart.domain.model.ItemOperation;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.utilities.BreadcrumbsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/cart")

public class CartViewController {


    private final SessionCartService sessionCartService;
    private final BreadcrumbsService breadcrumbsService;

    public CartViewController(SessionCartService sessionCartService, BreadcrumbsService breadcrumbsService) {
        this.sessionCartService = sessionCartService;
        this.breadcrumbsService = breadcrumbsService;
    }

    @GetMapping()
    public String index(Model model) {

        model.addAttribute("step", "cart"); // do dynamicznej wizualizacji postępu zamówienia
        model.addAttribute("cart", sessionCartService.getCart());
        model.addAttribute("totalCostShippingIncluded", sessionCartService.getTotalCostShippingIncluded());
        model.addAttribute("shipping", sessionCartService.getShippingCost());

        return "cart/index";
    }


    @PostMapping("/update/{productId}")
    public String updateCartItemQuantity(@PathVariable("productId") UUID productId,
                                         @RequestParam String action, @RequestParam int qty, Model model) {

        if (action.equals("increase")) {
            sessionCartService.itemOperation(productId, ItemOperation.INCREASE);

        } else if (action.equals("decrease")) {
            sessionCartService.itemOperation(productId, ItemOperation.DECREASE);
        }


        return "redirect:/cart";
    }

    @GetMapping("/delete/{productId}")
    public String deleteCartItemView(@PathVariable("productId") UUID productId, RedirectAttributes redirectAttributes) {
        try {
            sessionCartService.itemOperation(productId, ItemOperation.REMOVE);
            redirectAttributes.addFlashAttribute("message", Message.info("Product removed"));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", Message.error("Unknown error"));
        }

        return "redirect:/cart";
    }


}


