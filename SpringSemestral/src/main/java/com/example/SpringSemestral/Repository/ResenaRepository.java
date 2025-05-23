package com.example.SpringSemestral.Repository;

import com.example.SpringSemestral.Model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    List<Resena> findByProduct_Id(int productId);
    List<Resena> findByCliente_Id(int clienteId);
}

