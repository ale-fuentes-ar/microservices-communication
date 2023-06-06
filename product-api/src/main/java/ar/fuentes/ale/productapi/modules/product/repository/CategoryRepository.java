package ar.fuentes.ale.productapi.modules.product.repository;

import ar.fuentes.ale.productapi.modules.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
