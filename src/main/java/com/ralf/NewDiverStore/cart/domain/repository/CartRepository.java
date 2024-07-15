package com.ralf.NewDiverStore.cart.domain.repository;


import com.ralf.NewDiverStore.cart.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

}
