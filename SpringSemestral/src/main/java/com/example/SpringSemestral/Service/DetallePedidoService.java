package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.DetallePedido;
import com.example.SpringSemestral.Repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DetallePedidoService {
    @Autowired
    private DetallePedidoRepository detalleRepository;
    // Obtener un detalle específico por ID
    public Optional<DetallePedido> getOptionalDetalle(int id) {
        return detalleRepository.findById(id);
    }
    // Listar todos los detalles de un pedido
    public List<DetallePedido> listarPorPedido(int pedidoId) {
        return detalleRepository.findByPedido_Id(pedidoId);
    }
}
