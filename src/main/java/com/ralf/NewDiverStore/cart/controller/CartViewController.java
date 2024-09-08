package com.ralf.NewDiverStore.cart.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

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

@PostMapping("/increase")
    public String increaseQuantity(){
//TODO implement method
        return "redirect:/order/cart";
}

}


