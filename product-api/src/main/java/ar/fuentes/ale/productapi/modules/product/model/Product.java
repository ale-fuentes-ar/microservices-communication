package ar.fuentes.ale.productapi.modules.product.model;

import ar.fuentes.ale.productapi.modules.category.model.Category;
import ar.fuentes.ale.productapi.modules.product.dto.ProductRequest;
import ar.fuentes.ale.productapi.modules.supplier.model.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "FK_SUPPLIER", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "FK_CATEGORY", nullable = false)
    private Category category;

    @Column(name = "QUANTITY_AVAILABLE", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "CREATE_AT", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @PrePersist
    public void prePersist(){
        this.createAt = LocalDateTime.now();
    }

    public static Product of(ProductRequest reques,
                             Supplier supplier,
                             Category category){

        return Product
                .builder()
                .name(reques.getName())
                .quantityAvailable(reques.getQuantityAvailable())
                .supplier(supplier)
                .category(category)
                .build();

    }

}