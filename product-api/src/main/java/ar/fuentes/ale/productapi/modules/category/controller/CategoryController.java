package ar.fuentes.ale.productapi.modules.category.controller;

import ar.fuentes.ale.productapi.config.messages.SuccessResponse;
import ar.fuentes.ale.productapi.modules.category.dto.CategoryRequest;
import ar.fuentes.ale.productapi.modules.category.dto.CategoryResponse;
import ar.fuentes.ale.productapi.modules.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest request) {
        return categoryService.save(request);
    }

    @GetMapping
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable Integer id) {
        return categoryService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<CategoryResponse> findByName(@PathVariable String name) {
        return categoryService.findByName(name);
    }

    @PutMapping("{id}")
    public CategoryResponse update(@RequestBody CategoryRequest request, @PathVariable Integer id) {
        return categoryService.update(request, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return categoryService.delete(id);
    }

}
