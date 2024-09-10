package com.ralf.NewDiverStore.cart.service;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.domain.model.ItemOperation;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import com.ralf.NewDiverStore.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
public class SessionCartService {

    private final Cart cart;

    private final ProductService productService;

    private final ProductRepository productRepository;

    public SessionCartService(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.cart = new Cart();
    }


    public BigDecimal getTotalCostShippingIncluded(){
        return cart.getTotalCost();
    }

    public BigDecimal getShippingCost(){
        return cart.getTotalCost().subtract(cart.getSum());
    }

    public void addProductToCart(Product product) {
        cart.addItem(product);
    }


    public void removeProductFromCart(Product product) {
        cart.removeItem(product);
    }

    public void removeAllProducts(Product product) {
        cart.removeAllItems(product);
    }



    public int counter() {
        return cart.getCounter();
    }

    public void itemOperation(UUID productId, ItemOperation itemOperation) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            switch (itemOperation) {
                case INCREASE -> addProductToCart(product);
                case DECREASE -> removeProductFromCart(product);
                case REMOVE -> removeAllProducts(product);
                default -> throw new IllegalArgumentException();
            }
        }
    }
}
