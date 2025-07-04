package com.example.SpringSemestral.Repository;

import com.example.SpringSemestral.Model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    List<Factura> findByPedido_Cliente_Id(int clienteId);

    List<Factura> findByFechaEmisionBetween(LocalDate desde, LocalDate hasta);
    Optional<Factura> findByPedido_Id(int pedidoId);
    List<Factura> findByCliente_Id(int clienteId);

    List<Factura> findAll();

    void deleteAllByPedido_Id(int pedidoId);
}

