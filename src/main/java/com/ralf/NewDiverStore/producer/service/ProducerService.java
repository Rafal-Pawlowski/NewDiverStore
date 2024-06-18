package com.ralf.NewDiverStore.producer.service;

import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.domain.repository.ProducerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProducerService {

    private final ProducerRepository producerRepository;

    public ProducerService(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    @Transactional
    public Producer createProducer(Producer producerRequest) {
        Producer producer = new Producer(producerRequest.getName());
        producer.setDescription(producerRequest.getDescription());
        return producerRepository.save(producer);
    }

    @Transactional(readOnly = true)
    public Page<Producer> getProducers(Pageable pageable) {
        return getProducers(null, pageable);
    }
    @Transactional(readOnly = true)
    public Page<Producer> getProducers(String search, Pageable pageable) {
        if(search==null){
            return producerRepository.findAll(pageable);
        } else {
            return producerRepository.findByNameContainingIgnoreCase(search, pageable);
        }
    }

    @Transactional(readOnly = true)
    public Producer getSingleProducer(UUID id) {
        Optional<Producer> optionalProducer = producerRepository.findById(id);
        if(optionalProducer.isPresent()){
            Producer producer = optionalProducer.get();
            return producer;
        } else {
            throw new EntityNotFoundException("Producer with id: " + id + " not found");
        }
    }

    @Transactional
    public Producer updateProducer(UUID id, Producer producerRequest) {
        Optional<Producer> optionalProducer = producerRepository.findById(id);
        if(optionalProducer.isPresent()){
            Producer producer =  optionalProducer.get();
            producer.setName(producerRequest.getName());
            producer.setDescription(producerRequest.getDescription());
           return producerRepository.save(producer);
        } else {
            throw new EntityNotFoundException("Producer with id: " + id + " not found");
        }
    }

    @Transactional
    public void deleteProducer(UUID id) {
        producerRepository.deleteById(id);
    }
}
