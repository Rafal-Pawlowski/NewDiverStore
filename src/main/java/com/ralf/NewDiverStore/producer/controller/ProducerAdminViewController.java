package com.ralf.NewDiverStore.producer.controller;

import com.ralf.NewDiverStore.producer.service.ProducerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;


@Controller
@RequestMapping("admin/producers")
public class ProducerAdminViewController {


    public final ProducerService producerService;

    public ProducerAdminViewController(ProducerService producerService) {
        this.producerService = producerService;
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

}
