package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Envio;
import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Service.EnvioService;
import com.example.SpringSemestral.Service.PedidoService;
import com.example.SpringSemestral.Assembler.EnvioModelAssembler;
import com.example.SpringSemestral.Service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/envios")
@Tag(name = "Envíos", description = "Gestión de envíos de pedidos")
public class EnvioController {
    @Autowired
    private EnvioService envioService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private EnvioModelAssembler assembler;
    @Autowired
    private FacturaService facturaService;
    @Operation(summary = "Listar todos los envíos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envíos encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay envíos registrados")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> listarEnvios() {
        List<Envio> envios = envioService.listarEnvios();
        if (envios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<Envio>> modelos = envios.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Envio>> collection = CollectionModel.of(modelos,
                linkTo(methodOn(EnvioController.class).listarEnvios()).withSelfRel());

        return ResponseEntity.ok(collection);
    }
    @Operation(summary = "Asignar un pedido a un envío")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o pedido ya asignado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PostMapping("/asignar")
    public ResponseEntity<EntityModel<Envio>> asignarPedidoAEnvio(
            @RequestParam Integer pedidoId,
            @RequestParam String ruta) {
        Pedido pedido = pedidoService.getPedidoById(pedidoId);
        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        if (pedido.getEnvio() != null) {
            return ResponseEntity.badRequest().build();
        }
        Envio envio = envioService.asignarPedidoAEnvio(pedidoId, ruta);
        EntityModel<Envio> model = assembler.toModel(envio);
        return ResponseEntity.ok(model);
    }
    @Operation(summary = "Actualizar estado de entrega de un envío")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Estado inválido o envío no encontrado")
    })
    @PutMapping("/actualizar-estado/{envioId}")
    public ResponseEntity<EntityModel<Envio>> actualizarEstadoEntrega(
            @PathVariable Long envioId,
            @RequestParam String estado) {
        try {
            Envio updated = envioService.actualizarEstadoEntrega(envioId, estado);
            return ResponseEntity.ok(assembler.toModel(updated));

        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @Operation(summary = "Obtener envíos por cliente")
    @ApiResponse(responseCode = "200", description = "Envíos encontrados")
    @GetMapping("/por-cliente")
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> obtenerEnviosPorCliente(
            @RequestParam int clienteId) {
        List<Envio> envios = envioService.obtenerPorCliente(clienteId);
        if (envios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<Envio>> modelos = envios.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(modelos,
                linkTo(methodOn(EnvioController.class)
                        .obtenerEnviosPorCliente(clienteId)).withSelfRel()));
    }
    @Operation(summary = "Obtener un envío por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío encontrado"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<EntityModel<Envio>> verPorPedido(@PathVariable int pedidoId) {
        return envioService.getByPedidoId(pedidoId)             // Optional<Envio>
                .map(assembler::toModel)                            // Optional<EntityModel<Envio>>
                .map(ResponseEntity::ok)                            // Optional<ResponseEntity<…>>
                .orElse(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
    @Operation(summary = "Obtener un envío por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío encontrado"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> obtenerEnvioPorId(@PathVariable Long id) {
        return envioService.getEnvioById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @Operation(summary = "Eliminar un envío")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío eliminado"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEnvio(@PathVariable Long id) {
        try {
            envioService.eliminarEnvio(id);
            return ResponseEntity.ok("Envío eliminado correctamente");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Envío no encontrado");
        }
    }
}
