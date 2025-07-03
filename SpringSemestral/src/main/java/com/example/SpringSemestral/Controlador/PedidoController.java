package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Repository.PedidoRepository;
import com.example.SpringSemestral.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private PedidoRepository pedidoRepository;
    @PostMapping
    public String crearPedido(@RequestBody Pedido pedido,
                              @RequestParam(required = false) String cupon) {
        return pedidoService.crearPedido(pedido, cupon);
    }
    @GetMapping("/{id}/seguimiento")
    public String seguimiento(@PathVariable int id) {
        Pedido pedido = pedidoService.getPedidoById(id);
        if (pedido == null) return "Pedido no encontrado";
        return "Estado actual del pedido: " + pedido.getEstado();
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPorCliente(@PathVariable int clienteId) {
        return pedidoService.listarPedidosPorCliente(clienteId);
    }
    @PutMapping("/{id}/confirmar-pago")
    public String confirmarPago(@PathVariable int id) {
        return pedidoService.confirmarPago(id);
    }

    @GetMapping("/{id}")
    public Pedido obtenerPedido(@PathVariable int id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPedido(@PathVariable int id) {
        try {
            String mensaje = pedidoService.eliminarPedido(id);

            if (mensaje.contains("PENDIENTE pueden")) {
                return ResponseEntity.status(400).body(mensaje);
            }
            return ResponseEntity.ok(mensaje);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar el pedido: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable int id) {
        String resultado = pedidoService.cancelarPedido(id);
        if (resultado.contains("no encontrado")) {
            return ResponseEntity.status(404).body(resultado);
        } else if (resultado.contains("pendientes")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

}
