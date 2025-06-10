package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Factura;
import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;

    @GetMapping("/cliente/{clienteId}")
    public List<Factura> porCliente(@PathVariable int clienteId) {
        return facturaRepository.findByPedido_Cliente_Id(clienteId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerFacturaPorId(@PathVariable int id) {
        return facturaRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok("Factura no encontrada"));
    }
    @GetMapping
    public List<Factura> verTodas() {
        return facturaRepository.findAll();
    }

    @GetMapping("/por-fecha")
    public List<Factura> porFecha(
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
        return ResponseEntity.ok("Factura eliminada correctamente");
    }
}
