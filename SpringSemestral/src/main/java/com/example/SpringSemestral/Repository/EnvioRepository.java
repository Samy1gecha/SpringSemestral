package com.example.SpringSemestral.Repository;

import com.example.SpringSemestral.Model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByPedido_Cliente_Id(int clienteId);
    Optional<Envio> findByPedido_Id(int pedidoId);
}

