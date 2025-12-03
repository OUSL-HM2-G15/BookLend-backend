package com.booklend.backend.controllers;

import com.booklend.backend.models.Category;
import com.booklend.backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "${frontend.url}")
public class CategoryController {

    @Autowired // Dependency injection
    private CategoryService categoryService;

    @GetMapping // Endpoint to get all categories
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
