package com.ralf.NewDiverStore.cart.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cartId")
public class CartViewController {

    private final CartService cartService;

    public CartViewController(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cartId")
    public UUID cartId() {
        Cart newCart = cartService.saveNewCart(new Cart());
        return newCart.getId();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addToCart(@RequestParam("productId") UUID productId,
                                       @RequestParam("quantity") int quantity,
                                       @RequestParam("cartId") UUID cartId) {
        try {
            cartService.addProductToCart(cartId, productId, quantity);
            return ResponseEntity.ok("Product added to cart");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product to cart");
        }
    }


    @GetMapping("/view")
    public String viewCart(@ModelAttribute("cartId") UUID cartId, Model model) {
        Cart cart = cartService.getorCreateCart(cartId);
//        double shippingCost;
//
//        if (cart.getTotal() >= 200.00) {
//            shippingCost = 0;
//        } else {
//            shippingCost = 25.00;
//        }

        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("cartTotal", cart.getTotal());
        model.addAttribute("shippingCost", 20);
        model.addAttribute("total", cart.getTotal() + 20);
        return "cart/index";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam("productId") UUID productId,
                             @RequestParam("quantity") int quantity,
                             @ModelAttribute("cartId") UUID cartId) {
        cartService.updateProductQuantity(cartId, productId, quantity);
        return "redirect:/cart/view";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("productId") UUID productId, @ModelAttribute("cartId") UUID cartId) {
        cartService.removeProductFromCart(cartId, productId);
        return "redirect:/cart/view";
    }

}
