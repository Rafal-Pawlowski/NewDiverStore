package com.ralf.NewDiverStore.cart.service;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.UUID;

@Service
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionCartService {

    private final Cart cart;

    private final ProductService productService;

    public SessionCartService(ProductService productService) {
        this.productService = productService;
        this.cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }

    public void addProductToCart(Product product){
        cart.addItem(product);
    }

    public void removeProductFromCart(Product product){
        cart.removeItem(product);
    }

    public int counter(){
        return cart.getCounter();
    }

    public void itemOperation(UUID productId){

    }


}
