package com.ralf.NewDiverStore.cart.controller;


import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("api/v1/cart")
public class CartApiController {

    private final CartService cartService;

    public CartApiController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Cart addCart(@RequestParam("cartId") UUID cartId,
                        @RequestParam("productId")UUID productId,
                        @RequestParam("quantity")int quantity){

        return cartService.addProductToCart(cartId, productId, quantity);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public Cart updateCart(@RequestParam("cartId")UUID cartId,
                           @RequestParam("productId")UUID productId,
                           @RequestParam("quantity")int quantity){
        return cartService.updateProductQuantity(cartId, productId, quantity);
    }

    @DeleteMapping("/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Cart removeFromCart(@RequestParam("cartId") UUID cartId, @RequestParam("productId") UUID productId) {
        return cartService.removeProductFromCart(cartId, productId);
    }

    @GetMapping("/{cartId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Cart viewCart(@PathVariable UUID cartId) {
        return cartService.getCart(cartId);
    }

}
