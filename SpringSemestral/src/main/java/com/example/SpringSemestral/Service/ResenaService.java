package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Repository.ResenaRepository;
import com.example.SpringSemestral.Repository.ProductRepository;
import com.example.SpringSemestral.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository reseñaRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ResenaRepository resenaRepository;

    public String crear(int clienteId, int productId, String comentario, int calificacion) {
        User cliente = userRepo.findById(clienteId).orElse(null);
        Product product = productRepo.findById(productId).orElse(null);

        if (cliente == null || product == null) return "Cliente o producto no encontrado";

        Resena resena = new Resena(0, comentario, calificacion, cliente, product);
        reseñaRepo.save(resena);
        return "Reseña creada";
    }

    public List<Resena> verPorProduct(int productId) {
        return resenaRepository.findByProduct_Id(productId);
    }

    public List<Resena> verPorCliente(int clienteId) {
        return resenaRepository.findByCliente_Id(clienteId);
    }
}
