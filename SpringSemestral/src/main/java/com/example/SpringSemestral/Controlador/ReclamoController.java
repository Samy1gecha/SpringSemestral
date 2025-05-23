package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Reclamo;
import com.example.SpringSemestral.Service.ReclamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reclamos")
public class ReclamoController {

    @Autowired
    private ReclamoService reclamoService;

    @PostMapping
    public String crearReclamo(
            @RequestParam int clienteId,
            @RequestParam int pedidoId,
            @RequestParam String motivo
    ) {
        return reclamoService.crearReclamo(clienteId, pedidoId, motivo);
    }

    @GetMapping
    public List<Reclamo> verTodos() {
        return reclamoService.obtenerTodos();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Reclamo> verPorCliente(@PathVariable int clienteId) {
        return reclamoService.obtenerPorCliente(clienteId);
    }

    @PutMapping("/{reclamoId}/estado")
    public String cambiarEstado(
            @PathVariable int reclamoId,
            @RequestParam String estado
    ) {
        return reclamoService.cambiarEstado(reclamoId, estado);
    }
}
