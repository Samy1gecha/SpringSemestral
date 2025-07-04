package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Service.ResenaService;
import com.example.SpringSemestral.Assembler.ResenaModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/resenas")
@Tag(name = "Reseñas", description = "Gestión CRUD de reseñas")
public class ResenaController {

    @Autowired private ResenaService resenaService;
    @Autowired private ResenaModelAssembler assembler;

    @Operation(summary = "Crear reseña", description = "Publica una nueva reseña")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Resena>> crear(@RequestBody Resena resena) {
        Resena guardada = resenaService.crearDesdeObjeto(resena);
        return ResponseEntity
                .created(linkTo(methodOn(ResenaController.class).verPorId(guardada.getId())).toUri())
                .body(assembler.toModel(guardada));
    }

    @Operation(summary = "Listar todas las reseñas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseñas encontradas"),
            @ApiResponse(responseCode = "204", description = "No hay reseñas publicadas"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> verTodas() {
        List<Resena> lista = resenaService.verTodasResenas();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        List<EntityModel<Resena>> modelos = lista.stream().map(assembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(modelos, linkTo(methodOn(ResenaController.class).verTodas()).withSelfRel()));
    }

    @Operation(summary = "Obtener reseña por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontró la reseña"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Resena>> verPorId(@PathVariable int id) {
        return resenaService.verPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Eliminar reseña", description = "Borra una reseña por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña eliminada"),
            @ApiResponse(responseCode = "404", description = "No se encontró la reseña"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable int id) {
        boolean ok = resenaService.eliminarPorId(id);
        if (!ok) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar reseñas de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseñas encontradas"),
            @ApiResponse(responseCode = "204", description = "No hay reseñas para este producto"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> obtenerPorProducto(@PathVariable int productoId) {
        List<Resena> lista = resenaService.verPorProduct(productoId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        List<EntityModel<Resena>> modelos = lista.stream().map(assembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(modelos, linkTo(methodOn(ResenaController.class).obtenerPorProducto(productoId)).withSelfRel()));
    }

    @Operation(summary = "Listar reseñas de un cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseñas encontradas"),
            @ApiResponse(responseCode = "204", description = "No hay reseñas de este cliente"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> verPorCliente(@PathVariable int clienteId) {
        List<Resena> lista = resenaService.verPorCliente(clienteId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        List<EntityModel<Resena>> modelos = lista.stream().map(assembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(modelos, linkTo(methodOn(ResenaController.class).verPorCliente(clienteId)).withSelfRel()));
    }
}
