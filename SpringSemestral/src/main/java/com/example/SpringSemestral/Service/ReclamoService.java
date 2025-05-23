package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Model.Reclamo;
import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Repository.PedidoRepository;
import com.example.SpringSemestral.Repository.ReclamoRepository;
import com.example.SpringSemestral.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReclamoService {

    @Autowired
    private ReclamoRepository reclamoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public String crearReclamo(int clienteId, int pedidoId, String motivo) {
        User cliente = userRepository.findById(clienteId).orElse(null);
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);

        if (cliente == null || pedido == null) {
            return "Cliente o pedido no encontrado";
        }

        Reclamo reclamo = new Reclamo();
        reclamo.setCliente(cliente);
        reclamo.setPedido(pedido);
        reclamo.setMotivo(motivo);
        reclamo.setEstado("PENDIENTE");

        reclamoRepository.save(reclamo);
        return "Reclamo creado con éxito";
    }

    public List<Reclamo> obtenerTodos() {
        return reclamoRepository.findAll();
    }

    public List<Reclamo> obtenerPorCliente(int clienteId) {
        return reclamoRepository.findByCliente_Id(clienteId);
    }

    public String cambiarEstado(int reclamoId, String nuevoEstado) {
        Reclamo reclamo = reclamoRepository.findById(reclamoId).orElse(null);
        if (reclamo == null) return "Reclamo no encontrado";

        if (!nuevoEstado.equalsIgnoreCase("ACEPTADO") &&
                !nuevoEstado.equalsIgnoreCase("RECHAZADO")) {
            return "Estado inválido. Use ACEPTADO o RECHAZADO.";
        }

        reclamo.setEstado(nuevoEstado.toUpperCase());
        reclamoRepository.save(reclamo);
        return "Estado del reclamo actualizado";
    }
}

