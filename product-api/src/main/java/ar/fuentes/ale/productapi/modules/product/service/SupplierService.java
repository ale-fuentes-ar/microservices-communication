package ar.fuentes.ale.productapi.modules.product.service;

import ar.fuentes.ale.productapi.config.exception.ValidationException;
import ar.fuentes.ale.productapi.modules.supplier.dto.SupplierRequest;
import ar.fuentes.ale.productapi.modules.supplier.dto.SupplierResponse;
import ar.fuentes.ale.productapi.modules.supplier.model.Supplier;
import ar.fuentes.ale.productapi.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    SupplierRepository supplierRepository;

    public SupplierResponse save(SupplierRequest request){
        ValidateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    private void ValidateSupplierNameInformed(SupplierRequest request){
        if(isEmpty(request.getName())){
            throw new ValidationException("The supplier name was not infromed!");
        }
    }
}
