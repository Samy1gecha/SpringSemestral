package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public String crearPedido(Pedido pedido) {
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("PENDIENTE");
        pedido.getDetalles().forEach(d -> d.setPedido(pedido)); // vincula detalles al pedido
        pedidoRepository.save(pedido);
        return "Pedido creado con éxito";
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPedidosPorCliente(int clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }
}
