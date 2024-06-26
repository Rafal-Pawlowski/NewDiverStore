package com.ralf.NewDiverStore.product.domain.repository;

import com.ralf.NewDiverStore.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findProductByCategoryId(UUID categoryId);

    List<Product> findProductByProducerId(UUID producerId);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

