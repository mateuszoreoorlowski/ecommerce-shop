package pl.edu.ecommerceshop.catalog.service;

import pl.edu.ecommerceshop.catalog.dto.CategoryPatchRequest;
import pl.edu.ecommerceshop.catalog.dto.CategoryRequest;
import pl.edu.ecommerceshop.catalog.dto.CategoryResponse;
import pl.edu.ecommerceshop.catalog.model.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    Category getCategory(Long id);

    CategoryResponse partialUpdateCategory(Long id, CategoryPatchRequest request);


}
