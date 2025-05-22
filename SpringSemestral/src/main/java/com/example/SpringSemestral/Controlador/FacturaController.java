package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Factura;
import com.example.SpringSemestral.Repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/por-fecha")
    public List<Factura> porFecha(
            @RequestParam("desde") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return facturaRepository.findByFechaEmisionBetween(desde, hasta);
    }
}
