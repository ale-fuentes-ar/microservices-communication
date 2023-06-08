package ar.fuentes.ale.productapi.modules.supplier.repository;

import ar.fuentes.ale.productapi.modules.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
