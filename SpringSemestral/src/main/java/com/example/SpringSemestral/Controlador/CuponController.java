package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Cupon;
import com.example.SpringSemestral.Service.CuponService;
import com.example.SpringSemestral.Assembler.CuponModelAssembler;

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
@RequestMapping("/cupones")
@Tag(name = "Cupones", description = "CRUD de cupones con HATEOAS")
public class CuponController {

    @Autowired private CuponService service;
    @Autowired private CuponModelAssembler assembler;

    @Operation(summary = "Crear un cupón")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cupón creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Cupon>> create(@RequestBody Cupon c) {
        Cupon saved = service.create(c);
        return ResponseEntity
                .created(linkTo(methodOn(CuponController.class).getById(saved.getId())).toUri())
                .body(assembler.toModel(saved));
    }

    @Operation(summary = "Listar todos los cupones")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cupones encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay cupones")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cupon>>> getAll() {
        List<Cupon> list = service.getAll();
        if (list.isEmpty()) return ResponseEntity.noContent().build();
        List<EntityModel<Cupon>> models = list.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                CollectionModel.of(models,
                        linkTo(methodOn(CuponController.class).getAll()).withSelfRel()
                )
        );
    }

    @Operation(summary = "Obtener un cupón por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cupón encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró el cupón")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cupon>> getById(@PathVariable int id) {
        return service.getById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Eliminar un cupón")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminación exitosa"),
            @ApiResponse(responseCode = "404", description = "No se encontró el cupón")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        if (!service.deleteById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
}