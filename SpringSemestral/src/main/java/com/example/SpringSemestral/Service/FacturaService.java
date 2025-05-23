package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Factura;
import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }
    public List<Factura> verTodasFacturas() {
        return facturaRepository.findAll();
    }
    public List<Factura> buscarPorCliente(int clienteId) {
        return facturaRepository.findByCliente_Id(clienteId);
    }

    public List<Factura> buscarPorFecha(LocalDate desde, LocalDate hasta) {
        return facturaRepository.findByFechaEmisionBetween(desde, hasta);
    }
}

