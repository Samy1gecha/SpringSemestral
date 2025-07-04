package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Reclamo;
import com.example.SpringSemestral.Repository.ReclamoRepository;
import com.example.SpringSemestral.Service.ReclamoService;
import com.example.SpringSemestral.Assembler.ReclamoModelAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/reclamos")
@Tag(name = "Reclamos", description = "Gestión de reclamos de pedidos")
public class ReclamoController {

    @Autowired
    private ReclamoService reclamoService;

    @Autowired
    private ReclamoRepository reclamoRepository;

    @Autowired
    private ReclamoModelAssembler assembler;

    @Operation(summary = "Crear un nuevo reclamo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamo creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<String> crearReclamo(
            @RequestParam int clienteId,
            @RequestParam int pedidoId,
            @RequestParam String motivo
    ) {
        String resultado = reclamoService.crearReclamo(clienteId, pedidoId, motivo);
        if (resultado.startsWith("Reclamo creado")) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.badRequest().body(resultado);
    }

    @Operation(summary = "Listar todos los reclamos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamos encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay reclamos")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Reclamo>>> obtenerTodos() {
        List<Reclamo> lista = reclamoService.obtenerTodos();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<Reclamo>> modelos = lista.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Reclamo>> collection = CollectionModel.of(modelos,
                linkTo(methodOn(ReclamoController.class).obtenerTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Listar reclamos de un cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamos encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay reclamos para ese cliente")
    })
    @GetMapping("/cliente/{id}")
    public ResponseEntity<CollectionModel<EntityModel<Reclamo>>> obtenerPorCliente(
            @PathVariable int id) {

        List<Reclamo> lista = reclamoService.obtenerPorCliente(id);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<Reclamo>> modelos = lista.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(modelos,
                linkTo(methodOn(ReclamoController.class).obtenerPorCliente(id)).withSelfRel()));
    }

    @Operation(summary = "Obtener reclamo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamo encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró reclamo")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Reclamo>> obtenerPorId(@PathVariable int id) {
        return reclamoRepository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Cambiar estado de un reclamo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "404", description = "Reclamo no encontrado")
    })
    @PutMapping("/{reclamoId}/estado")
    public ResponseEntity<String> cambiarEstado(
            @PathVariable int reclamoId,
            @RequestParam String estado
    ) {
        String resultado = reclamoService.cambiarEstado(reclamoId, estado);
        if (resultado.equals("Estado del reclamo actualizado")) {
            return ResponseEntity.ok(resultado);
        } else if (resultado.equals("Reclamo no encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        return ResponseEntity.badRequest().body(resultado);
    }

    @Operation(summary = "Eliminar un reclamo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamo eliminado"),
            @ApiResponse(responseCode = "404", description = "Reclamo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReclamo(@PathVariable int id) {
        if (!reclamoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reclamo no encontrado");
        }
        reclamoRepository.deleteById(id);
        return ResponseEntity.ok("Reclamo eliminado correctamente");
    }
}

