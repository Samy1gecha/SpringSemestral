package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Factura;
import com.example.SpringSemestral.Service.PedidoService;
import com.example.SpringSemestral.Repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SpringSemestral.Service.FacturaService;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private FacturaService facturaService;
    @Autowired
    private PedidoService pedidoService;
    @GetMapping("/cliente/{clienteId}")
    public List<Factura> buscarPorCliente(@PathVariable int clienteId) {
        return facturaRepository.findByPedido_Cliente_Id(clienteId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerFacturaPorId(@PathVariable int id) {
        return facturaRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok("Factura no encontrada"));
    }
    @GetMapping
    public ResponseEntity<?> verTodas() {
        List<Factura> facturas = facturaService.verTodas();
        //Si el pago no se a confirmado manda el mensaje de abajo
        if (facturas.isEmpty()) {
            return ResponseEntity.ok("📭 No se ha confirmado el pago de ningún pedido, por lo tanto no se ha generado ninguna factura.");
        }
        //Al ver la vista general, se mostrara resumido, al buscar por id, una vista completa
        List<Map<String, Object>> resumenes = facturas.stream().map(f -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("idFactura", f.getId());
            map.put("fechaEmision", f.getFechaEmision());
            map.put("estado", f.getEstado());
            map.put("ID cliente", f.getCliente().getId());
            // Obtener primer producto del pedido
            String nombreProducto = f.getPedido().getDetalles().isEmpty()
                    ? "Sin productos"
                    : f.getPedido().getDetalles().get(0).getProducto().getNombre();

            map.put("producto", nombreProducto);
            return map;
        }).toList();

        return ResponseEntity.ok(resumenes);
    }
    @GetMapping("/por-fecha")
    public List<Factura> buscarPorFecha(
            @RequestParam("desde") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return facturaRepository.findByFechaEmisionBetween(desde, hasta);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFactura(@PathVariable int id) {
        if (!facturaRepository.existsById(id)) {
            return ResponseEntity.ok("Factura no encontrada");
        }
        facturaRepository.deleteById(id);
        return ResponseEntity.ok("Las Facturas Generadas no se pueden eliminar");
    }
    @PutMapping("/{id}/confirmar-pago")
    public ResponseEntity<String> confirmarPago(@PathVariable int id) {
        try {
            String mensaje = pedidoService.confirmarPago(id);
            if (mensaje.contains("PENDIENTE")) {
                return ResponseEntity.badRequest().body(mensaje);
            }
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error al confirmar pago: " + e.getMessage());
        }
    }
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstadoFactura(@PathVariable int id,
                                                       @RequestParam("estado") String nuevoEstado) {
        Optional<Factura> optional = facturaRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Factura no encontrada");
        }
        //Validar que el estado sea permitido
        List<String> validos = List.of("Emitida", "Pagada", "Anulada");
        if (!validos.contains(nuevoEstado)) {
            return ResponseEntity.badRequest()
                    .body("⚠️ Estado inválido. Opciones válidas: " + validos);
        }
        Factura factura = optional.get();
        factura.setEstado(nuevoEstado);
        facturaRepository.save(factura);

        return ResponseEntity.ok("✅ Estado de la factura actualizado a: " + nuevoEstado);
    }


}
