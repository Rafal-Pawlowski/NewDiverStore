package com.ralf.NewDiverStore.producer.controller;

import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.service.ProducerService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
    public String indexView(Pageable pageable, Model model) {

        Page<Producer> producersPage = producerService.getProducers(pageable);
        model.addAttribute("producersPage", producersPage);
        paging(model, producersPage);

        return "admin/producer/index";
    }

    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id, RedirectAttributes ra) {
        List<Product> productsList = productService.getProductsByProducerId(id);
        if (productsList.isEmpty()) {
            producerService.deleteProducer(id);
            ra.addFlashAttribute("message", Message.info("Producer removed"));
        } else {
            ra.addFlashAttribute("message", Message.error("Producer cannot be removed. Please delete related products first"));
        }
        return "redirect:/admin/producers";
    }

    @GetMapping("{id}/edit")
    public String editView(Model model, @PathVariable UUID id) {
        model.addAttribute("producer", producerService.getSingleProducer(id));
        return "admin/producer/edit";
    }

    @PostMapping("{id}/edit")
    public String edit(@PathVariable UUID id,
                       @Valid @ModelAttribute("producer") Producer producer,
                       BindingResult bindingResult,
                       RedirectAttributes ra,
                       Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("producer", producer);
            model.addAttribute("message", Message.error("Data writing error"));
            return "admin/producer/edit";
        }

        try {
            producerService.updateProducer(id, producer);
            ra.addFlashAttribute("message", Message.info("Producer updated"));
        } catch (Exception e) {
            model.addAttribute("producer", producer);
            model.addAttribute("message", Message.error("Unknown data writing error"));
            return "admin/producer/edit";
        }
        return "redirect:/admin/producers";
    }

    @GetMapping("{id}")
    public String singleView(@PathVariable UUID id, Model model) {
        model.addAttribute("producer", producerService.getSingleProducer(id));
        model.addAttribute("products", productService.getProductsByProducerId(id));
        return "admin/producer/single";
    }

    @GetMapping("add")
    public String addView(Model model) {
        model.addAttribute("producer", new Producer());
        return "admin/producer/add";
    }

    @PostMapping("add")
    public String add(
            @Valid @ModelAttribute("producer") Producer producer,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("producer", producer);
            model.addAttribute("message", Message.error("Producer adding error"));
            return "admin/producer/add";
        }

        try{
            producerService.createProducer(producer);
            ra.addFlashAttribute("message", Message.info("Producer added"));
        }catch (Exception e){
            model.addAttribute("producer", producer);
            model.addAttribute("message", Message.error("Unknown data writing error"));
            return "admin/producer/add";
        }
        return "redirect:/admin/producers";
    }


    private void paging(Model model, Page page) {
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }

}




















