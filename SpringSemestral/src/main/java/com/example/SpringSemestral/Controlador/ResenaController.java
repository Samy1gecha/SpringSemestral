package com.example.SpringSemestral.Controlador;
import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SpringSemestral.Repository.ResenaRepository;
import java.util.List;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;
    @Autowired
    private ResenaRepository resenaRepository;
    @PostMapping
    public String crear(@RequestBody Resena resena) {
        System.out.println("Reseña publicada: " + resena);
        return resenaService.crearDesdeObjeto(resena);
    }

    @GetMapping
    public ResponseEntity<?> verTodas() {
        List<Resena> lista = resenaService.verTodasResenas();
        if (lista.isEmpty()) {
            return ResponseEntity.ok("📭 No hay reseñas publicadas hasta el momento.");
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<?> obtenerPorProducto(@PathVariable int productoId) {
        List<Resena> resenas = resenaRepository.findByProduct_Id(productoId);
        if (resenas.isEmpty()) {
            return ResponseEntity.ok("📭 Este producto aún no tiene reseñas publicadas.");
        }
        return ResponseEntity.ok(resenas);
    }
    @DeleteMapping("/{id}")
    public String eliminarPorId(@PathVariable int id) {
        return resenaService.eliminarPorId(id); //
    }

    @GetMapping("/product/{id}")
    public List<Resena> verPorProduct(@PathVariable int id) {
        return resenaService.verPorProduct(id);
    }

    @GetMapping("/cliente/{id}")
    public List<Resena> verPorCliente(@PathVariable int id) {
        return resenaService.verPorCliente(id);
    }
}
