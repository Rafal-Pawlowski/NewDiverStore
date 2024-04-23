package com.ralf.NewDiverStore.category.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryViewController {



    @GetMapping("index")
    public String indexView(){
        return "category/index";
    }

    @GetMapping("categories")
    public String categoriesView(){
        return "category/list";
    }

}
