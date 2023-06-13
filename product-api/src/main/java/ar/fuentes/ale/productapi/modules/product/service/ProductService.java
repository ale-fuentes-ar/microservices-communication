package ar.fuentes.ale.productapi.modules.product.service;

import ar.fuentes.ale.productapi.config.exception.ValidationException;
import ar.fuentes.ale.productapi.config.messages.SuccessResponse;
import ar.fuentes.ale.productapi.modules.category.service.CategoryService;
import ar.fuentes.ale.productapi.modules.product.dto.ProductRequest;
import ar.fuentes.ale.productapi.modules.product.dto.ProductResponse;
import ar.fuentes.ale.productapi.modules.product.model.Product;
import ar.fuentes.ale.productapi.modules.product.repository.ProductRepository;
import ar.fuentes.ale.productapi.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {

    private static final Integer ZERO = 0;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    SupplierService supplierService;

    public ProductResponse findByIdResponse(Integer id) {
        validateInformedId(id);
        return ProductResponse.of(findById(id));
    }

    public List<ProductResponse> findAll() {
        return productRepository
                .findAll()
                .stream().map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByName(String name) {
        if (isEmpty(name)) {
            throw new ValidationException("The supplier NAME was not informed!");
        }
        return productRepository
                .findByNameIgnoreCaseContaining(name)
                .stream().map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public Product findById(Integer id) {
        validateInformedId(id);

        return productRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no product for the given ID"));
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId) {
        if (isEmpty(categoryId)) {
            throw new ValidationException("The product` category ID was not informed!");
        }

        return productRepository
                .findByCategoryId(categoryId)
                .stream().map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId) {
        if (isEmpty(supplierId)) {
            throw new ValidationException("The product` category ID was not informed!");
        }

        return productRepository
                .findBySupplierId(supplierId)
                .stream().map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse save(ProductRequest request) {

        validateProductDataInformed(request);
        validateCategoryAndSupplierId(request);

        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById((request.getSupplierId()));
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    public ProductResponse update(ProductRequest request, Integer id) {
        validateProductDataInformed(request);
        validateCategoryAndSupplierId(request);
        validateInformedId(id);

        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById((request.getSupplierId()));
        var product = Product.of(request, supplier, category);
        product.setId(id);

        productRepository.save(product);
        return ProductResponse.of(product);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);
        productRepository.deleteById(id);
        return SuccessResponse.create("The product was deleted!");
    }

    public Boolean existsByCategoryId(Integer categoryId) {
        return productRepository.existsByCategoryId(categoryId);
    }

    public Boolean existsBySupplierId(Integer supplierId) {
        return productRepository.existsBySupplierId(supplierId);
    }

    private void validateProductDataInformed(ProductRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationException("The product's name was not informed!");
        }
        if (isEmpty(request.getQuantityAvailable())) {
            throw new ValidationException("The product's quantity was not informed!");
        }
        if (request.getQuantityAvailable() <= ZERO) {
            throw new ValidationException("The product's quantity should not be less o equal to zero!");
        }
    }

    private void validateCategoryAndSupplierId(ProductRequest request) {
        if (isEmpty(request.getCategoryId())) {
            throw new ValidationException("The category ID was not informed!");
        }
        if (isEmpty(request.getSupplierId())) {
            throw new ValidationException("The supplier ID was not informed!");
        }

    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The product ID was not informed!");
        }
    }
}
