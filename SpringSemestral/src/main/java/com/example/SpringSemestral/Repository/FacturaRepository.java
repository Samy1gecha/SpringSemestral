package com.example.SpringSemestral.Repository;

import com.example.SpringSemestral.Model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    List<Factura> findByPedido_Cliente_Id(int clienteId);

    List<Factura> findByFechaEmisionBetween(LocalDate desde, LocalDate hasta);
}

