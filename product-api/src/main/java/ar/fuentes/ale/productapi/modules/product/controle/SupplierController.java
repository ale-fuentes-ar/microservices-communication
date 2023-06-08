package ar.fuentes.ale.productapi.modules.product.controle;

import ar.fuentes.ale.productapi.modules.product.dto.SupplierRequest;
import ar.fuentes.ale.productapi.modules.product.dto.SupplierResponse;
import ar.fuentes.ale.productapi.modules.product.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    SupplierService supplierService;

    @PostMapping
    public SupplierResponse save(@RequestBody SupplierRequest request){
        return supplierService.save(request);
    }

}
