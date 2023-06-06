package ar.fuentes.ale.productapi.modules.product.model;

import ar.fuentes.ale.productapi.modules.product.dto.CategoryRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORY")
public class Category {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "NAME", nullable = false)
    private String name;

    public static Category of(CategoryRequest request){
        var category = new Category();
        BeanUtils.copyProperties(request, category);
        return category;
    }
}
