package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Método auxiliar para validar roles autorizados
    private boolean isAuthorized(String role) {return "ADMIN".equals(role) || "EMPLEADO".equals(role);}

    // Mostrar todos los productos (sin restricción de roles)
    @GetMapping
    public List<Product> getProducts() {return productService.getAllProducts();}

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {return productService.getProduct(id);}

    // Crear producto (solo ADMIN o EMPLEADO)
    @PostMapping
    public ResponseEntity<String> postProduct(@RequestBody Product product, @RequestHeader("X-Role") String role) {
        if (role == null || !isAuthorized(role)) {
            // Si no hay rol o no está autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
        }

        return ResponseEntity.ok(productService.addProduct(product));
    }

    // Eliminar producto (solo ADMIN o EMPLEADO)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id, @RequestHeader("X-Role") String role) {
        if (role == null || !isAuthorized(role)) {
            // Si no hay rol o no está autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
        }
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    // Actualizar producto (solo ADMIN o EMPLEADO)
    @PutMapping
    public ResponseEntity<String> putProduct(@RequestBody Product product, @RequestHeader("X-Role") String role) {
        if (role == null || !isAuthorized(role)) {
            // Si no hay rol o no está autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
        }
        return ResponseEntity.ok(productService.updateProduct(product));}
}

