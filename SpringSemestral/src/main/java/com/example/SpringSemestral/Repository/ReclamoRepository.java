package com.example.SpringSemestral.Repository;

import com.example.SpringSemestral.Model.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReclamoRepository extends JpaRepository<Reclamo, Integer> {
    List<Reclamo> findByCliente_Id(int clienteId);
    List<Reclamo> findByPedido_Id(int pedidoId);

}

