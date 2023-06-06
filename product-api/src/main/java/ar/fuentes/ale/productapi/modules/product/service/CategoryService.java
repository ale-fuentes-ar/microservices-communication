package ar.fuentes.ale.productapi.modules.product.service;

import ar.fuentes.ale.productapi.config.exception.ValidationException;
import ar.fuentes.ale.productapi.modules.product.dto.CategoryRequest;
import ar.fuentes.ale.productapi.modules.product.dto.CategoryResponse;
import ar.fuentes.ale.productapi.modules.product.model.Category;
import ar.fuentes.ale.productapi.modules.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponse save(CategoryRequest request){
        validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    private void validateCategoryNameInformed(CategoryRequest request){
        if(isEmpty(request.getName())){
            throw new ValidationException("The category description was not informed.");
        }
    }

}
