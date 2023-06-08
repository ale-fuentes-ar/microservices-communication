package ar.fuentes.ale.productapi.modules.supplier.dto;

import ar.fuentes.ale.productapi.modules.supplier.model.Supplier;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SupplierResponse {

    private Integer Id;
    private String Name;

    public static SupplierResponse of(Supplier supplier){
        var response = new SupplierResponse();
        BeanUtils.copyProperties(supplier, response);
        return  response;
    }

}