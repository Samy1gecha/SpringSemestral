package com.example.SpringSemestral.Repository;

import com.example.SpringSemestral.Model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    // Obtener todos los detalles asociados a un pedido específico
    List<DetallePedido> findByPedido_Id(int pedidoId);
}

