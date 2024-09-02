package com.ralf.NewDiverStore.cart.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/order")
public class CartViewController {

    @GetMapping("/cart")
    public String index() {

        return "cart/index";
    }
    }

