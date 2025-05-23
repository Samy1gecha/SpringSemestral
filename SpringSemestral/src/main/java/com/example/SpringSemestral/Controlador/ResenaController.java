package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @PostMapping
    public String crear(@RequestBody Resena resena) {
        System.out.println("Objeto recibido: " + resena);
        return resenaService.crearDesdeObjeto(resena);
    }
    @GetMapping("/product/{id}")
    public List<Resena> verPorProduct(@PathVariable int id) {
        return resenaService.verPorProduct(id);
    }
    @GetMapping
    public List<Resena> verTodas() {
        return resenaService.verTodasResenas(); // este método debe retornar resenaRepository.findAll()
    }

    @GetMapping("/cliente/{id}")
    public List<Resena> verPorClient(@PathVariable int id) {
        return resenaService.verPorCliente(id);
    }
}
