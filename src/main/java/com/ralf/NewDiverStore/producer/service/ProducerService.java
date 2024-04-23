package com.ralf.NewDiverStore.producer.service;

import com.ralf.NewDiverStore.producer.domain.model.Producer;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class ProducerService {


    public Producer createProducer(Producer producer) {
        producer.setId(UUID.randomUUID());
        return producer;
    }

    public List<Producer> getProducers() {
        List<Producer> producers = List.of(new Producer("Producer 1"),
                new Producer("Producer 2"),
                new Producer("Producer 3"));
        return producers;
    }

    public Producer getSingleProducer(UUID id) {
        return new Producer("Single producer" + id);

    }

    public Producer updateProducer(UUID id, Producer producerRequest) {
    Producer producer = new Producer("Producer before update");
    producer.setName(producerRequest.getName());
    return producer;

    }

    public void deleteProducer(UUID id) {
    }
}
