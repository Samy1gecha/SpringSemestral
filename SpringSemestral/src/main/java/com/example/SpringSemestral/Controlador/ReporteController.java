package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/ventas-totales")
    public ResponseEntity<?> getTotalVentas() {
        Double total = reporteService.totalVentas();
        if (total == null || total == 0.0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No hay ventas registradas.");
        }
        return ResponseEntity.ok(total);
    }

    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<?> getProductosMasVendidos() {
        List<Object[]> lista = reporteService.productosMasVendidos();
        if (lista == null || lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No hay productos vendidos.");
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/total-por-cliente")
    public ResponseEntity<?> getTotalPorCliente() {
        List<Object[]> lista = reporteService.totalPorCliente();
        if (lista == null || lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No hay ventas por cliente.");
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/ventas-por-fecha")
    public ResponseEntity<?> getTotalVentasPorFecha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        Double total = reporteService.totalVentasPorFecha(desde, hasta);
        if (total == null || total == 0.0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No hay ventas entre las fechas indicadas.");
        }
        return ResponseEntity.ok(total);
    }
}

