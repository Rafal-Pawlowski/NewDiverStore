package com.ralf.NewDiverStore.cart.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/order")

public class CartViewController {


    private final SessionCartService sessionCartService;

    public CartViewController(SessionCartService sessionCartService) {
        this.sessionCartService = sessionCartService;
    }

    @GetMapping("/cart")
    public String index(Model model) {

        model.addAttribute("cart", sessionCartService.getCart());


        return "cart/index";
    }



    @PostMapping("/update/{productId}")
    public String updateCartItemQuantity(@PathVariable("productId") UUID productId,
                                         @RequestParam String action, @RequestParam int qty, Model model) {

        if (action.equals("increase")) {
            sessionCartService.addProductToCart(productId);

        } else if (action.equals("decrease")) {
            sessionCartService.removeProductFromCart(productId);
        }

        model.addAttribute("cart", sessionCartService.getCart());
        return "redirect:/order/cart";
    }


}


