package com.ralf.NewDiverStore.producer.controller;

import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.service.ProducerService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import com.ralf.NewDiverStore.utilities.Pagination;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ralf.NewDiverStore.utilities.Pagination.*;


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
    public String indexView(
            @RequestParam(name = "s", required = false)String search,
            @RequestParam(name = "field", required = false, defaultValue = "id")String field,
            @RequestParam(name = "direction", required = false, defaultValue = "asc")String direction,
            @RequestParam(name = "page", required = false, defaultValue = "0")int page,
            @RequestParam(name = "size", required = false, defaultValue = "10")int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), field);

        String reverseSort = null;
        if("asc".equals(direction)){
            reverseSort = "desc";
        }else {
            reverseSort = "asc";
        }

        Page<Producer> producersPage = producerService.getProducers(search, pageable);
        model.addAttribute("producersPage", producersPage);
        model.addAttribute("search", search);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseSort", reverseSort);
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



}




















