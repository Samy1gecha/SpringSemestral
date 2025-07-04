package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "Estadísticas de ventas")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Operation(summary = "Ventas totales", description = "Suma de ventas registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ventas totales obtenidas"),
            @ApiResponse(responseCode = "404", description = "No hay ventas registradas"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @GetMapping("/ventas-totales")
    public ResponseEntity<EntityModel<Double>> getTotalVentas() {
        Double total = reporteService.totalVentas();
        if (total == null || total == 0.0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        EntityModel<Double> model = EntityModel.of(total,
                linkTo(methodOn(ReporteController.class).getTotalVentas()).withSelfRel()
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Productos más vendidos", description = "Listado de productos y cantidades vendidas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida"),
            @ApiResponse(responseCode = "404", description = "No hay productos vendidos"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<CollectionModel<EntityModel<Object[]>>> getProductosMasVendidos() {
        List<Object[]> lista = reporteService.productosMasVendidos();
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        var modelos = lista.stream()
                .map(row -> EntityModel.of(row,
                        linkTo(methodOn(ReporteController.class).getProductosMasVendidos()).withSelfRel()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                CollectionModel.of(modelos,
                        linkTo(methodOn(ReporteController.class).getProductosMasVendidos()).withSelfRel()
                )
        );
    }
    @Operation(summary = "Total de ventas por cliente", description = "Suma de ventas agrupadas por cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos obtenidos"),
            @ApiResponse(responseCode = "404", description = "No hay ventas para ningún cliente"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @GetMapping("/total-por-cliente")
    public ResponseEntity<CollectionModel<EntityModel<Object[]>>> getTotalPorCliente() {
        List<Object[]> lista = reporteService.totalPorCliente();
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        var modelos = lista.stream()
                .map(row -> EntityModel.of(row,
                        linkTo(methodOn(ReporteController.class).getTotalPorCliente()).withSelfRel()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                CollectionModel.of(modelos,
                        linkTo(methodOn(ReporteController.class).getTotalPorCliente()).withSelfRel()
                )
        );
    }

    @Operation(summary = "Ventas por fecha", description = "Suma de ventas entre dos fechas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ventas obtenidas"),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas"),
            @ApiResponse(responseCode = "404", description = "No hay ventas en el rango"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @GetMapping("/ventas-por-fecha")
    public ResponseEntity<EntityModel<Double>> getTotalVentasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        if (desde == null || hasta == null || hasta.isBefore(desde)) {
            return ResponseEntity.badRequest().build();
        }
        Double total = reporteService.totalVentasPorFecha(desde, hasta);
        if (total == null || total == 0.0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        EntityModel<Double> model = EntityModel.of(total,
                linkTo(methodOn(ReporteController.class).getTotalVentasPorFecha(desde, hasta)).withSelfRel()
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Eliminar reporte", description = "Borra un registro de DetallePedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eliminación exitosa"),
            @ApiResponse(responseCode = "404", description = "No se encontró el registro"),
            @ApiResponse(responseCode = "405", description = "Método no permitido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReporte(@PathVariable int id) {
        boolean ok = reporteService.eliminarReporte(id);
        if (!ok) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reporte no encontrado");
        }
        return ResponseEntity.ok("Reporte eliminado correctamente");
    }
}