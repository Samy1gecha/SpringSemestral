package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reseñas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @PostMapping
    public String crear(
            @RequestParam int clienteId,
            @RequestParam int productId,
            @RequestParam String comentario,
            @RequestParam int calificacion
    ) {
        return resenaService.crear(clienteId, productId, comentario, calificacion);
    }

    @GetMapping("/product/{id}")
    public List<Resena> verPorProduct(@PathVariable int id) {
        return resenaService.verPorProduct(id);
    }

    @GetMapping("/cliente/{id}")
    public List<Resena> verPorClient(@PathVariable int id) {
        return resenaService.verPorCliente(id);
    }
}
