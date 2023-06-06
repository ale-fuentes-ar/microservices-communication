package ar.fuentes.ale.productapi.modules.product.dto;

import ar.fuentes.ale.productapi.modules.product.model.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CategoryResponse {

    private Integer id;
    private String name;

    public static CategoryResponse of(Category category){
        var response = new CategoryResponse();
        BeanUtils.copyProperties(category, response);
        return response;
    }
}
