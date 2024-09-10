package com.ralf.NewDiverStore.cart.service;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import com.ralf.NewDiverStore.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Optional;
import java.util.UUID;

@Service
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionCartService {

    private final Cart cart;

    private final ProductService productService;

    private final ProductRepository productRepository;

    public SessionCartService(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }

    public void addProductToCart(Product product){
        cart.addItem(product);
    }

    public void addProductToCart(UUID productId){
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            addProductToCart(product);
        } else{
            throw new EntityNotFoundException("Entity with id " + productId + " not found");
        }
    }

    public void removeProductFromCart(Product product){
        cart.removeItem(product);
    }
 public void removeProductFromCart(UUID productId){
     Optional<Product> optionalProduct = productRepository.findById(productId);
     if(optionalProduct.isPresent()){
         Product product = optionalProduct.get();
         removeProductFromCart(product);
     } else{
         throw new EntityNotFoundException("Entity with id " + productId + " not found");
     }
    }

    public int counter(){
        return cart.getCounter();
    }

    public void itemOperation(UUID productId){

    }


}
