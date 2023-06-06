package ar.fuentes.ale.productapi.modules.product.repository;

import ar.fuentes.ale.productapi.modules.product.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
