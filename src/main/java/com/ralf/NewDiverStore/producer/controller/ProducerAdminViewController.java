package com.ralf.NewDiverStore.producer.controller;

import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.service.ProducerService;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@RequestMapping("admin/producers")
public class ProducerAdminViewController {


    public final ProducerService producerService;

    public final ProductService productService;

    public ProducerAdminViewController(ProducerService producerService, ProductService productService) {
        this.producerService = producerService;
        this.productService = productService;
    }

    @GetMapping
    public String indexView(Model model){
        model.addAttribute("producers", producerService.getProducers());
    return "admin/producer/index";
    }

    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id){
        producerService.deleteProducer(id);
        return "redirect:/admin/producers";
    }

    @GetMapping("{id}/edit")
    public String editView(Model model, @PathVariable UUID id){
        model.addAttribute("producer", producerService.getSingleProducer(id));
        return "admin/producer/edit";
    }

    @PostMapping("{id}/edit")
    public String edit(@ModelAttribute("producer") Producer producer, @PathVariable UUID id){
        producerService.updateProducer(id, producer);
        return "redirect:/admin/producers";
    }

    @GetMapping("{id}")
    public String singleView(@PathVariable UUID id, Model model){
        model.addAttribute("producer", producerService.getSingleProducer(id));
        model.addAttribute("products", productService.getProductsByProducerId(id));
        return "admin/producer/single";
    }

}
