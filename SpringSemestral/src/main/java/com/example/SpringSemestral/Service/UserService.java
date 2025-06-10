package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
        // Verificamos si el email ya existe para evitar duplicados
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Error: Ya existe un usuario con ese email";
        }

        try {
            userRepository.save(user); // Inserta en la base de datos
            return "Usuario agregado con éxito";
        } catch (DataIntegrityViolationException e) {
            // En caso de que por algún motivo se intente insertar duplicado
            return "Error: Ya existe un usuario con ese email (excepción)";
        }
    }

    public String updateUser(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.save(user); // Actualiza si ya existe
            return "Usuario actualizado con éxito";
        }
        return "Usuario no encontrado";
    }

    public String deleteUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "Usuario no encontrado";

        // 1. Eliminar reclamos
        var reclamos = reclamoRepository.findByCliente_Id(id);
        if (!reclamos.isEmpty()) {
            reclamoRepository.deleteAll(reclamos);
        }

        // 2. Eliminar pedidos (los pedidos también eliminan detalles, envíos y facturas por cascade)
        var pedidos = pedidoRepository.findByCliente_Id(id);
        if (!pedidos.isEmpty()) {
            pedidoRepository.deleteAll(pedidos);
        }

        // 3. Eliminar reseñas
        var resenas = resenaRepository.findByCliente_Id(id);
        if (!resenas.isEmpty()) {
            resenaRepository.deleteAll(resenas);
        }

        // 4. Finalmente, eliminar el usuario
        userRepository.deleteById(id);
        return "Usuario eliminado con éxito";
    }

    public User getUser(int id) {
        return userRepository.findById(id).orElse(null); // Retorna objeto o null
    }
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Retorna lista completa desde BD
    }
    public User getUserByEmail(String email) { //Buscar por email
        return userRepository.findByEmail(email).orElse(null);
    }

}
