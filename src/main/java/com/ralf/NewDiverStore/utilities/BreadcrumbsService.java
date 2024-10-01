package com.ralf.NewDiverStore.utilities;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BreadcrumbsService {

    public List<BreadcrumbItem> generateBreadcrumbs(String...paths){
        List<BreadcrumbItem> breadcrumbs = new ArrayList<>();
        StringBuilder currentPath = new StringBuilder();

        for(String path : paths){
            currentPath.append("/").append(path.toLowerCase().replace(" ", "-"));
            breadcrumbs.add(new BreadcrumbItem(path, currentPath.toString()));
        }
        return breadcrumbs;
    }

    public List<BreadcrumbItem> breadcrumbsHomeCategories(){
        List<BreadcrumbItem> breadcrumbItems = new ArrayList<>();
        breadcrumbItems.add(new BreadcrumbItem("home", "/index"));
        breadcrumbItems.add(new BreadcrumbItem("categories", "/categories"));

        return breadcrumbItems;
    }

    public List<BreadcrumbItem> breadcrumbsHomeNewest(){
        List<BreadcrumbItem> breadcrumbItems = new ArrayList<>();
        breadcrumbItems.add(new BreadcrumbItem("home", "/index"));
        breadcrumbItems.add(new BreadcrumbItem("newest", "/newest"));

        return breadcrumbItems;
    }

    public List<BreadcrumbItem> breadcrumbsHomeTop(){
        List<BreadcrumbItem> breadcrumbItems = new ArrayList<>();
        breadcrumbItems.add(new BreadcrumbItem("home", "/index"));
        breadcrumbItems.add(new BreadcrumbItem("top", "/top"));

        return breadcrumbItems;
    }


   public List<BreadcrumbItem> breadcrumbsHomeCategoriesCategoriesName(String categoryName, UUID categoryId){
        List<BreadcrumbItem> breadcrumbItems = new ArrayList<>();
        breadcrumbItems.add(new BreadcrumbItem("home", "/index"));
        breadcrumbItems.add(new BreadcrumbItem("categories", "/categories"));
        breadcrumbItems.add(new BreadcrumbItem(categoryName, "/categories/" + categoryId + "/products"));
        return breadcrumbItems;
    }


}
