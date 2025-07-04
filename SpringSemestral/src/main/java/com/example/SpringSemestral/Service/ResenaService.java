// src/main/java/com/example/SpringSemestral/Service/ResenaService.java
package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Repository.ResenaRepository;
import com.example.SpringSemestral.Repository.UserRepository;
import com.example.SpringSemestral.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Resena crearDesdeObjeto(Resena resena) {
        if (resena.getCliente() == null || resena.getProduct() == null) {
            throw new IllegalArgumentException("Debe incluir cliente y producto con sus IDs");
        }
        User cliente = userRepository.findById(resena.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Product product = productRepository.findById(resena.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        resena.setCliente(cliente);
        resena.setProduct(product);
        return resenaRepository.save(resena);
    }

    @Transactional(readOnly = true)
    public Optional<Resena> verPorId(int id) {
        return resenaRepository.findById(id);
    }

    @Transactional
    public boolean eliminarPorId(int id) {
        if (!resenaRepository.existsById(id)) {
            return false;
        }
        resenaRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<Resena> verTodasResenas() {
        return resenaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Resena> verPorProduct(int productId) {
        return resenaRepository.findByProduct_Id(productId);
    }

    @Transactional(readOnly = true)
    public List<Resena> verPorCliente(int clienteId) {
        return resenaRepository.findByCliente_Id(clienteId);
    }
}

