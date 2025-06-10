package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public Double totalVentas() {
        return reporteRepository.obtenerTotalVentas();
    }

    public List<Object[]> productosMasVendidos() {
        return reporteRepository.obtenerProductosMasVendidos();
    }

    public List<Object[]> totalPorCliente() {
        return reporteRepository.obtenerTotalPorCliente();
    }
    public Double totalVentasPorFecha(LocalDate desde, LocalDate hasta) {
        return reporteRepository.obtenerTotalVentasPorFecha(desde, hasta);
    }
    public boolean eliminarReporte(int id) {
        // Eliminar el reporte por id,
        if (reporteRepository.existsById(id)) {
            reporteRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
