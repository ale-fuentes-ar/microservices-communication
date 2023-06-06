package ar.fuentes.ale.productapi.modules.product.repository;

import ar.fuentes.ale.productapi.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
