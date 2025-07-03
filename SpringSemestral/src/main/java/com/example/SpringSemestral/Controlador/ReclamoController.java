package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Reclamo;
import com.example.SpringSemestral.Repository.ReclamoRepository;
import com.example.SpringSemestral.Service.ReclamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reclamos")
public class ReclamoController {

    @Autowired
    private ReclamoService reclamoService;
    @Autowired
    private ReclamoRepository reclamoRepository;
    @PostMapping
    public String crearReclamo(
            @RequestParam int clienteId,
            @RequestParam int pedidoId,
            @RequestParam String motivo
    ) {
        return reclamoService.crearReclamo(clienteId, pedidoId, motivo);
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        List<Reclamo> lista = reclamoService.obtenerTodos();
        if (lista.isEmpty()) {
            return ResponseEntity.ok("📭 No hay reclamos registrados hasta el momento.");
        }
        return ResponseEntity.ok(lista);
    }


    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> obtenerPorCliente(@PathVariable int id) {
        List<Reclamo> lista = reclamoService.obtenerPorCliente(id);
        if (lista.isEmpty()) {
            return ResponseEntity.ok("📭 Este cliente aún no ha registrado reclamos.");
        }
        return ResponseEntity.ok(lista);
    }


    @PutMapping("/{reclamoId}/estado")
    public String cambiarEstado(
            @PathVariable int reclamoId,
            @RequestParam String estado
    ) {
        return reclamoService.cambiarEstado(reclamoId, estado);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
        return reclamoRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok("📭 No se encontró ningún reclamo con ese ID."));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReclamo(@PathVariable int id) {
        if (!reclamoRepository.existsById(id)) {
            return ResponseEntity.ok("Reclamo no encontrado");
        }
        reclamoRepository.deleteById(id);
        return ResponseEntity.ok("Reclamo eliminado correctamente");
    }
}
