package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Service.ProductService;
import com.example.SpringSemestral.Assembler.ProductModelAssembler;
import com.example.SpringSemestral.Repository.ProductRepository;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Gestión de productos del sistema")
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private ProductModelAssembler assembler;

    private boolean isAuthorized(String role) {
        return "ADMIN".equalsIgnoreCase(role) || "EMPLEADO".equalsIgnoreCase(role);
    }

    @Operation(summary = "Obtener todos los productos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos encontrados"),
            @ApiResponse(responseCode = "404", description = "No hay productos registrados")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getProducts() {
        List<Product> productos = productService.getAllProducts();

        if (productos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(assembler.toCollectionModel(productos));
    }
    @Operation(summary = "Obtener producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> getProductById(@PathVariable int id) {
        Optional<Product> producto = productService.getOptionalProduct(id);
        if (producto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(assembler.toModel(producto.get()));

    }
    @Operation(summary = "Registrar producto", description = "Solo ADMIN o EMPLEADO pueden registrar productos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Product>> postProduct(@Valid @RequestBody Product product, @RequestHeader("X-Role") String role) {
        if (!isAuthorized(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(product));
    }
    @Operation(summary = "Eliminar producto", description = "Solo ADMIN o EMPLEADO pueden eliminar productos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id,
                                                @RequestHeader("X-Role") String role) {
        if (!isAuthorized(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok("Producto eliminado con éxito");
    }
    @Operation(summary = "Actualizar producto", description = "Solo ADMIN o EMPLEADO pueden actualizar productos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PutMapping
    public ResponseEntity<EntityModel<Product>> putProduct(@RequestBody Product product,
                                                           @RequestHeader("X-Role") String role) {
        if (!isAuthorized(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        productService.updateProduct(product);
        return ResponseEntity.ok(assembler.toModel(product));
    }
}