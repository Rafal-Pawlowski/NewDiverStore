package com.ralf.NewDiverStore.producer.domain.repository;

import com.ralf.NewDiverStore.producer.domain.model.Producer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, UUID> {
    Page<Producer> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
