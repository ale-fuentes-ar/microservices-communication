package ar.fuentes.ale.productapi.modules.category.repository;

import ar.fuentes.ale.productapi.modules.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
