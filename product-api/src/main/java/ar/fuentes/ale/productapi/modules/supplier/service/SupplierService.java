package ar.fuentes.ale.productapi.modules.supplier.service;

import ar.fuentes.ale.productapi.config.exception.ValidationException;
import ar.fuentes.ale.productapi.config.messages.SuccessResponse;
import ar.fuentes.ale.productapi.modules.product.service.ProductService;
import ar.fuentes.ale.productapi.modules.supplier.dto.SupplierRequest;
import ar.fuentes.ale.productapi.modules.supplier.dto.SupplierResponse;
import ar.fuentes.ale.productapi.modules.supplier.model.Supplier;
import ar.fuentes.ale.productapi.modules.supplier.repository.SupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor_ = { @Lazy })
public class SupplierService {


    private final SupplierRepository supplierRepository;
    @Lazy
    private final ProductService productService;

    public SupplierResponse findByIdResponse(Integer id){
        validateInformedId(id);

        return SupplierResponse.of(findById(id));
    }

    public List<SupplierResponse> findAll(){
        return supplierRepository
                .findAll()
                .stream().map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public List<SupplierResponse> findByName(String name){
        if(isEmpty(name)){
            throw new ValidationException("The supplier NAME was not informed!");
        }
        return supplierRepository
                .findByNameIgnoreCaseContaining(name)
                .stream().map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public Supplier findById(Integer id){
        validateInformedId(id);

        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given ID"));
    }

    public SupplierResponse save(SupplierRequest request){
        validateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse update(SupplierRequest request, Integer id){
        validateSupplierNameInformed(request);
        validateInformedId(id);

        var supplier  =Supplier.of(request);
        supplier.setId(id);

        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    public SuccessResponse delete(Integer id){
        validateInformedId(id);
        if(productService.existsBySupplierId(id)){
            throw new ValidationException("you cannot delete this supplier because it's already defined by a product");
        }

        supplierRepository.deleteById(id);
        return SuccessResponse.create("The supplier was deleted!");
    }

    private void validateSupplierNameInformed(SupplierRequest request){
        if(isEmpty(request.getName())){
            throw new ValidationException("The supplier name was not infromed!");
        }
    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The supplier ID was not informed!");
        }
    }
}
