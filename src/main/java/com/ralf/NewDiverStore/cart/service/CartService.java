package com.ralf.NewDiverStore.cart.service;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.domain.repository.CartRepository;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public Cart getCart(UUID cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    @Transactional
    public Cart addProductToCart(UUID cartId, UUID productId, int quantity) {
        Cart cart = getCart(cartId);
        Product product = productService.getSingleProduct(productId);
        cart.addItem(product, quantity);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateProductQuantity(UUID cartId, UUID productId, int quantity) {
        Cart cart = getCart(cartId);
        Product product = productService.getSingleProduct(productId);
        cart.updateItem(product, quantity);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeProductFromCart(UUID cartId, UUID productId) {
        Cart cart = getCart(cartId);
        Product product = productService.getSingleProduct(productId);
        cart.removeItem(product);
        return cartRepository.save(cart);
    }

    public Cart saveNewCart(Cart cart) {
        return cartRepository.save(cart);
    }
}
