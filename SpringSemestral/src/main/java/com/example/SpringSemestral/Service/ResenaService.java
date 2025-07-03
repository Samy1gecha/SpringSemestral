package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Repository.ResenaRepository;
import com.example.SpringSemestral.Repository.ProductRepository;
import com.example.SpringSemestral.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ResenaRepository resenaRepository;

    public String crearDesdeObjeto(@RequestBody Resena resena) {
        System.out.println("Cliente: " + resena.getCliente());
        System.out.println("Producto: " + resena.getProduct());

        if (resena.getCliente() == null || resena.getProduct() == null) {
            return "Debe incluir cliente y producto con sus IDs";
        }

        User cliente = userRepository.findById(resena.getCliente().getId()).orElse(null);
        Product product = productRepository.findById(resena.getProduct().getId()).orElse(null);

        if (cliente == null || product == null) {
            return "Cliente o producto no encontrado";
        }

        resena.setCliente(cliente);
        resena.setProduct(product);

        resenaRepository.save(resena);
        return "Reseña creada con éxito";
    }
    public Resena verPorId(int id) {
        return resenaRepository.findById(id).orElse(null);
    }

    public String eliminarPorId(int id) {
        Resena resena = resenaRepository.findById(id).orElse(null);
        if (resena == null) return "Reseña no encontrada";

        resenaRepository.delete(resena);
        return "Reseña eliminada con éxito";
    }
    public List<Resena> verTodasResenas() {
        return resenaRepository.findAll();
    }

    public List<Resena> verPorProduct(int productId) {
        return resenaRepository.findByProduct_Id(productId);
    }

    public List<Resena> verPorCliente(int clienteId) {
        return resenaRepository.findByCliente_Id(clienteId);
    }
}
