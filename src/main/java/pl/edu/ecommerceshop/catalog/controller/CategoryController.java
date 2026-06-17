package pl.edu.ecommerceshop.catalog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.ecommerceshop.catalog.dto.CategoryPatchRequest;
import pl.edu.ecommerceshop.catalog.dto.CategoryRequest;
import pl.edu.ecommerceshop.catalog.dto.CategoryResponse;
import pl.edu.ecommerceshop.catalog.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@Valid @RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @PatchMapping("/{id}")
    public CategoryResponse partialUpdate(
            @PathVariable Long id,
            @Valid @RequestBody CategoryPatchRequest request
    ) {
        return categoryService.partialUpdateCategory(id, request);
    }
}