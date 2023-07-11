package ar.fuentes.ale.productapi.modules.category.service;

import ar.fuentes.ale.productapi.config.exception.ValidationException;
import ar.fuentes.ale.productapi.config.messages.SuccessResponse;
import ar.fuentes.ale.productapi.modules.category.dto.CategoryRequest;
import ar.fuentes.ale.productapi.modules.category.dto.CategoryResponse;
import ar.fuentes.ale.productapi.modules.category.model.Category;
import ar.fuentes.ale.productapi.modules.category.repository.CategoryRepository;
import ar.fuentes.ale.productapi.modules.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor( onConstructor_ = { @Lazy })
public class CategoryService {

    private final CategoryRepository categoryRepository;
    @Lazy
    private final ProductService productService;

    public CategoryResponse findByIdResponse(Integer id) {
//        return categoryRepository.findById(id)
//                .map(CategoryResponse::of)
//                .orElseThrow(() -> new ValidationException("There's no category for the given ID!"));

        validateInformedId(id);

        return CategoryResponse.of(findById(id));
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository
                .findAll()
                .stream().map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> findByName(String name) {
        if (isEmpty(name)) {
            throw new ValidationException("The category NAME was not informed!");
        }
        return categoryRepository
                .findByNameIgnoreCaseContaining(name)
                .stream().map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public Category findById(Integer id) {
        validateInformedId(id);

        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given ID"));
    }

    public CategoryResponse save(CategoryRequest request) {
        validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    public CategoryResponse update(CategoryRequest request, Integer id) {
        validateCategoryNameInformed(request);
        validateInformedId(id);

        var category = Category.of(request);
        category.setId(id);

        categoryRepository.save(category);
        return CategoryResponse.of(category);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);
        if (productService.existsByCategoryId(id)) {
            throw new ValidationException("you cannot delete this category because it's already defined by a product");
        }

        categoryRepository.deleteById(id);
        return SuccessResponse.create("The category was deleted!");
    }

    private void validateCategoryNameInformed(CategoryRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationException("The category NAME was not informed!");
        }
    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The category ID was not informed!");
        }
    }

}
