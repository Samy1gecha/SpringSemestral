package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.DetallePedido;
import com.example.SpringSemestral.Service.DetallePedidoService;
import com.example.SpringSemestral.Assembler.DetallePedidoModelAssembler;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/detalle")
@Tag(name = "Detalles de Pedido", description = "Visualización de los ítems comprados dentro de un pedido")
public class DetallePedidoController {

    @Autowired private DetallePedidoService detalleService;
    @Autowired private DetallePedidoModelAssembler assembler;

    @Operation(summary = "Ver detalle de pedido por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró el detalle")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DetallePedido>> verPorId(@PathVariable int id) {
        return detalleService.getOptionalDetalle(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Listar detalles de un pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalles encontrados"),
            @ApiResponse(responseCode = "404", description = "No hay detalles registrados para ese pedido")
    })
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<CollectionModel<EntityModel<DetallePedido>>> listarPorPedido(@PathVariable int pedidoId) {
        List<DetallePedido> lista = detalleService.listarPorPedido(pedidoId);
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(assembler.toCollectionModel(lista));
    }
}
