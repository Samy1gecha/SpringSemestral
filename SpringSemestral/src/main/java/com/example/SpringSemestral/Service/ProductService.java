package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getOptionalProduct(int id) {
        return productRepository.findById(id);
    }

    public void addProduct(Product product) {
        if (productRepository.existsByNombre(product.getNombre())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
        }
        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error al guardar el producto: datos duplicados o inválidos.");
        }
    }

    public void updateProduct(Product productoActualizado) {
        Optional<Product> optional = productRepository.findById(productoActualizado.getId());
        if (optional.isPresent()) {
            Product producto = optional.get();
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setStock(productoActualizado.getStock());
            productRepository.save(producto);
        } else {
            throw new NoSuchElementException("Producto no encontrado.");
        }
    }

    public void deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Producto no encontrado.");
        }
    }
}
