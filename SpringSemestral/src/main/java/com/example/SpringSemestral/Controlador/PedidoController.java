package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Service.PedidoService;
import com.example.SpringSemestral.Assembler.PedidoModelAssembler;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Gestión de pedidos del sistema")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private PedidoModelAssembler assembler;
    @Operation(summary = "Crear nuevo pedido")
    @PostMapping
    public ResponseEntity<String> crearPedido(@RequestBody Pedido pedido,
                                              @RequestParam(required = false) String cupon) {
        pedidoService.crearPedido(pedido, cupon);

        double total = pedido.getDetalles().stream()
                .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Pedido creado con total aproximado: $" + total);
    }
    @Operation(summary = "Ver estado de un pedido")
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<String> seguimiento(@PathVariable int id) {
        return pedidoService.getOptionalPedido(id)
                .map(pedido ->
                        ResponseEntity.ok("Estado actual del pedido: " + pedido.getEstado()))
                .orElse(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Pedido no encontrado"));
    }
    @Operation(summary = "Listar todos los pedidos")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> listarTodos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(assembler.toCollectionModel(pedidos));
    }
    @Operation(summary = "Listar pedidos por ID de cliente")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> getPedidosByClienteId(@PathVariable int clienteId) {
        List<Pedido> pedidos = pedidoService.listarPedidosPorCliente(clienteId);
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(assembler.toCollectionModel(pedidos));
    }
    @Operation(summary = "Confirmar pago de pedido")
    @PutMapping("/{id}/confirmar-pago")
    public ResponseEntity<String> confirmarPago(@PathVariable int id) {
        try {
            // llamo al service que ahora no devuelve String sino void
            pedidoService.confirmarPago(id);
            // si todo salió bien, devuelve 200 con mensaje estándar
            return ResponseEntity.ok("Pago confirmado y factura generada");
        } catch (NoSuchElementException e) {
            //cuando el pedido no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            //Si ya estaba PAGADO
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            //cualquier otro error inesperado
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al confirmar el pago: " + e.getMessage());
        }
    }
    @Operation(summary = "Obtener pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> obtenerPedido(@PathVariable int id) {
        return pedidoService.getOptionalPedido(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @Operation(summary = "Eliminar pedido")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPedido(@PathVariable int id) {
        try {
            // Llamás al service que ahora lanza IllegalStateException
            pedidoService.eliminarPedido(id);
            // Si todo OK
            return ResponseEntity.ok("✅ Pedido eliminado correctamente.");
        } catch (NoSuchElementException e) {
            // Cuando no existe el pedido
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage()); //"Pedido no encontrado"
        } catch (IllegalStateException e) {
            // Cuando el pedido no está PENDIENTE
            // e.getMessage() será:
            // "Solo se pueden eliminar pedidos con estado PENDIENTE."
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            // Cualquier otro error
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el pedido: " + e.getMessage());
        }
    }
    @Operation(summary = "Cancelar pedido")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable int id) {
        try {
            // Llamada al service que ya no devuelve String
            pedidoService.cancelarPedido(id);

            // Si todo salió bien
            return ResponseEntity.ok("Pedido cancelado correctamente");
        } catch (NoSuchElementException e) {
            // Cuando no existe el pedido
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalStateException e) {
            // Cuando el pedido no está en estado PENDIENTE
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            // Cualquier otro error inesperado
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cancelar el pedido: " + e.getMessage());
        }
    }
}
