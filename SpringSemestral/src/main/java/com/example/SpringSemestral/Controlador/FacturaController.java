package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Factura;
import com.example.SpringSemestral.Service.FacturaService;
import com.example.SpringSemestral.Assembler.FacturaModelAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/facturas")
@Tag(name = "Facturas", description = "Gestión de facturas del sistema")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    @Autowired
    private FacturaModelAssembler assembler;
    @Operation(summary = "Listar todas las facturas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facturas encontradas"),
            @ApiResponse(responseCode = "204", description = "No hay facturas generadas")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Factura>>> verTodas() {
        List<Factura> facturas = facturaService.verTodas();

        if (facturas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        CollectionModel<EntityModel<Factura>> model =
                assembler.toCollectionModel(facturas)
                        .add(linkTo(methodOn(FacturaController.class).verTodas()).withSelfRel());

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Obtener factura por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Factura>> getFacturaById(@PathVariable int id) {
        return facturaService.getOptionalFactura(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @Operation(summary = "Obtener todas las facturas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facturas encontradas"),
            @ApiResponse(responseCode = "204", description = "No hay facturas registradas")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Factura>> getAllFacturas() {
        List<Factura> facturas = facturaService.getAllFacturas();
        if (facturas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(facturas);
    }
    @Operation(summary = "Listar facturas de un cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facturas encontradas"),
            @ApiResponse(responseCode = "404", description = "No hay facturas para ese cliente")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CollectionModel<EntityModel<Factura>>> getByCliente(@PathVariable int clienteId) {
        List<Factura> facturas = facturaService.buscarPorCliente(clienteId);

        if (facturas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        CollectionModel<EntityModel<Factura>> model =
                assembler.toCollectionModel(facturas)
                        .add(linkTo(methodOn(FacturaController.class).getByCliente(clienteId)).withSelfRel());

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Listar facturas por rango de fecha")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facturas encontradas"),
            @ApiResponse(responseCode = "404", description = "No hay facturas en ese rango de fechas")
    })
    @GetMapping("/por-fecha")
    public ResponseEntity<CollectionModel<EntityModel<Factura>>> getByFecha(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        List<Factura> facturas = facturaService.buscarPorFecha(desde, hasta);

        if (facturas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        CollectionModel<EntityModel<Factura>> model =
                assembler.toCollectionModel(facturas)
                        .add(linkTo(methodOn(FacturaController.class).getByFecha(desde, hasta)).withSelfRel());

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Eliminar factura (no permitido)")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
            @ApiResponse(responseCode = "405", description = "Operación no permitida")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFactura(@PathVariable int id) {
        boolean exists = facturaService.existsById(id);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Factura no encontrada");
        }
        // Las facturas generadas no se pueden eliminar
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("⚠️ Las facturas generadas no se pueden eliminar");
    }
    @Operation(summary = "Cambiar estado de la factura")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(
            @PathVariable int id,
            @RequestParam("estado") String estado) {

        try {
            facturaService.cambiarEstado(id, estado);
            return ResponseEntity.ok("✅ Estado de la factura actualizado a: " + estado);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error al actualizar estado: " + e.getMessage());
        }
    }
}