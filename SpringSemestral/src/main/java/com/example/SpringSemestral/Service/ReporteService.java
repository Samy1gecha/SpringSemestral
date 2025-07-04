package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Repository.ReporteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    /** Suma el total de ventas de todos los DetallePedido */
    public Double totalVentas() {
        return reporteRepository.obtenerTotalVentas();
    }

    /** Productos más vendidos: nombre y cantidad */
    public List<Object[]> productosMasVendidos() {
        return reporteRepository.obtenerProductosMasVendidos();
    }

    /** Total de ventas agrupado por cliente (username, total) */
    public List<Object[]> totalPorCliente() {
        return reporteRepository.obtenerTotalPorCliente();
    }

    /** Suma total de ventas entre dos fechas */
    public Double totalVentasPorFecha(LocalDate desde, LocalDate hasta) {
        return reporteRepository.obtenerTotalVentasPorFecha(desde, hasta);
    }

    /** Elimina un “snapshot” de reporte (DetallePedido) por ID */
    @Transactional
    public boolean eliminarReporte(int id) {
        if (!reporteRepository.existsById(id)) {
            return false;
        }
        reporteRepository.deleteById(id);
        return true;
    }
}