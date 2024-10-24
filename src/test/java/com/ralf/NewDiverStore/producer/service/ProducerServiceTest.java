package com.ralf.NewDiverStore.producer.service;

import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.domain.repository.ProducerRepository;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import com.ralf.NewDiverStore.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ProducerServiceTest {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        producerRepository.deleteAll();
    }

    @Test
    void shouldCreateProducer() {
        //given
        Producer producerRequest = new Producer("Producer");
        producerRequest.setDescription("TestDescription");

        //when
        Producer result = producerService.createProducer(producerRequest);

        //then
        Optional<Producer> optionalProducer = producerRepository.findById(result.getId());
        assertThat(optionalProducer)
                .isPresent()
                .get()
                .extracting(Producer::getName, Producer::getDescription)
                .containsExactly(producerRequest.getName(), producerRequest.getDescription());
    }

    @Test
    void shouldGetProducers() {
        //given
        producerRepository.saveAll(List.of(
                new Producer("Producer1"),
                new Producer("Producer2"),
                new Producer("Producer3")));
        //when
        Page<Producer> result = producerService.getProducers(Pageable.unpaged());

        //then
        assertThat(result)
                .hasSize(3)
                .extracting(Producer::getName)
                .containsExactly("Producer1", "Producer2", "Producer3");

    }
    @Test
    void shouldGetProducersWithQuery() {
        //given
        String query = "xyz";
        Producer producer = new Producer("Producer2"+query);

        producerRepository.saveAll(List.of(
                new Producer("Producer1"),
                producer,
                new Producer("Producer3")));

        //when
        Page<Producer> result = producerService.getProducers(query, Pageable.unpaged());

        //then
        assertThat(result).hasSize(1)
                .extracting(Producer::getName)
                .containsExactly(producer.getName());

    }

    @Test
    void shouldGetSingleProducer() {
        //given
        Producer producer = producerRepository.save( new Producer("Producer"));

        //when
        Producer result = producerService.getSingleProducer(producer.getId());

        //then
        assertThat(result)
                .isNotNull()
                .extracting(Producer::getId, Producer::getName)
                .containsExactly(producer.getId(), producer.getName());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhileGettingSingleProducer() {
        //given
        UUID id = UUID.randomUUID();

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
        {
            producerService.getSingleProducer(id);
        });

        assertThat(exception.getMessage()).isEqualTo("Producer with id: " + id + " not found");

    }

    @Test
    void shouldUpdateExistingProducer() {
        //given
        Producer producerToUpdate = new Producer("ProducerBeforeUpdate");
        Producer producer = producerRepository.save( producerToUpdate);
        Producer request = new Producer("UpdatedProducer");
        request.setDescription("UpdatedDescription");

        //when
        Producer result = producerService.updateProducer(producer.getId(), request);

        //then
        assertThat(result)
                .isNotNull()
                .extracting(Producer::getId, Producer::getName, Producer::getDescription)
                .containsExactly(producer.getId(), request.getName(), request.getDescription());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhileUpdatingNotExistingProducer(){
        //given
        UUID id = UUID.randomUUID();
        Producer request = new Producer("UpdatedProducer");
        request.setDescription("UpdatedDescription");

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            producerService.updateProducer(id, request);
        });
        assertThat(exception.getMessage()).isEqualTo("Producer with id: " + id + " not found");
    }

    @Test
    void shouldDeleteProducer() {
        //given
        Producer producer = producerRepository.save(new Producer("Producer"));
        UUID id = producer.getId();

        //when
        Throwable throwable = catchThrowable(()-> producerService.deleteProducer(id));

        //then
        assertThat(throwable).isNull();
        assertThat(producerRepository.findById(id)).isEmpty();
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhileRemovingProducer(){
        //given
        UUID id = UUID.randomUUID();

        //when
        Throwable throwable = catchThrowable(()-> producerService.deleteProducer(id));

        //then
        assertThat(throwable).isNotNull().hasMessageMatching("Entity with id: "+ id + " do not exist");

    }


}