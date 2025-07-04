package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ReclamoRepository reclamoRepository;
    @Autowired private ResenaRepository resenaRepository;
    @Autowired private UserRepository userRepository;
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<User> getOptionalUser(int id) {
        return userRepository.findById(id);
    }
    public Optional<User> getOptionalUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public void addUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email.");
        }
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email (violación de integridad).");
        }
    }
    public void updateUser(int id, User updatedUser) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setRol(updatedUser.getRol());
            userRepository.save(user);
        } else {
            throw new NoSuchElementException("Usuario no encontrado para actualizar.");
        }
    }
    // Comprueba si existe un usuario por su ID
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    // Elimina un usuario por su ID; lanza NoSuchElementException si no existe
    public void deleteById(Integer id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        userRepository.delete(u);
    }
    @Transactional
    public String deleteUser(int id) {
        // Buscar usuario directamente
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        // Verificar si tiene pedidos
        boolean tienePedidos = !pedidoRepository.findByCliente_Id(id).isEmpty();
        if (tienePedidos) {
            // Marcar como inactivo
            user.setActivo(false);
            userRepository.save(user);
            return "El usuario tiene pedidos. Se marcó como inactivo.";
        }
        // Eliminar sus reseñas
        resenaRepository.deleteAll(resenaRepository.findByCliente_Id(id));
        // Eliminar reclamos relacionados a sus pedidos
        var pedidos = pedidoRepository.findByCliente_Id(id);
        for (var pedido : pedidos) {
            var reclamos = reclamoRepository.findByPedido_Id(pedido.getId());
            reclamoRepository.deleteAll(reclamos);
        }
        // Eliminar reclamos directos
        reclamoRepository.deleteAll(reclamoRepository.findByCliente_Id(id));
        // Limpiar relaciones antes de borrar pedidos
        for (var pedido : pedidos) {
            pedido.setFactura(null);
            pedido.setEnvio(null);
            if (pedido.getDetalles() != null) {
                pedido.getDetalles().clear();
            }
        }
        pedidoRepository.saveAll(pedidos);
        pedidoRepository.deleteAll(pedidos);
        // Finalmente, eliminar el usuario
        userRepository.delete(user);
        return "Usuario eliminado correctamente.";
    }

}