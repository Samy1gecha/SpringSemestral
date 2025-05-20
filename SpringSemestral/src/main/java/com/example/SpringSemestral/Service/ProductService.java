package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public String addProduct(Product product) {
        // Verifica si ya hay un producto con ese nombre
        if(productRepository.existsByNombre(product.getNombre())) {
            return "Error: Ya existe un producto con ese nombre";
        }
        productRepository.save(product);
        return "Producto agregado con éxito";
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public String updateProduct(Product product) {
        if (productRepository.existsById(product.getId())) {
            productRepository.save(product);
            return "Producto actualizado con éxito";
        }
        return "Producto no encontrado";
    }

    public String deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return "Producto eliminado con éxito";
        }
        return "Producto no encontrado";
    }
}