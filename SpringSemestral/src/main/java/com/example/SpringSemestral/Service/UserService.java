package com.example.SpringSemestral.Service;
import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ReclamoRepository reclamoRepository;
    @Autowired
    private ResenaRepository resenaRepository;
    @Autowired
    private UserRepository userRepository;

    public String addUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Error: Ya existe un usuario con ese email";
        }

        try {
            userRepository.save(user);
            return "Usuario agregado con éxito";
        } catch (DataIntegrityViolationException e) {
            return "Error: Ya existe un usuario con ese email (excepción)";
        }
    }

    public String updateUser(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.save(user);
            return "Usuario actualizado con éxito";
        }
        return "Usuario no encontrado";
    }

    @Transactional
    public String deleteUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "Usuario no encontrado";

        // Eliminar reseñas
        resenaRepository.deleteAll(resenaRepository.findByCliente_Id(id));

        // Obtener pedidos del usuario
        var pedidos = pedidoRepository.findByCliente_Id(id);

        // Eliminar reclamos ligados a cada pedido
        for (var pedido : pedidos) {
            var reclamosDelPedido = reclamoRepository.findByPedido_Id(pedido.getId());
            if (!reclamosDelPedido.isEmpty()) {
                reclamoRepository.deleteAll(reclamosDelPedido);
            }
        }

        // Eliminar reclamos directos del usuario (por si quedara alguno)
        reclamoRepository.deleteAll(reclamoRepository.findByCliente_Id(id));

        // Romper relaciones en pedidos (Factura, Envío, Detalles)
        for (var pedido : pedidos) {
            if (pedido.getFactura() != null) pedido.setFactura(null);
            if (pedido.getEnvio() != null) pedido.setEnvio(null);
            if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
                pedido.getDetalles().clear();
            }
        }

        // Guardar cambios y eliminar pedidos
        pedidoRepository.saveAll(pedidos);
        pedidoRepository.deleteAll(pedidos);

        // Eliminar el usuario
        userRepository.deleteById(id);

        return "Usuario eliminado con éxito";
    }

    public User getUser(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
