package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Envio;
import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Service.EnvioService;
import com.example.SpringSemestral.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Envio> listarEnvios() {
        return envioService.listarEnvios();
    }
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarPedidoAEnvio(@RequestParam Integer pedidoId, @RequestParam String ruta) {
        try {
            Pedido pedido = pedidoService.getPedidoById(pedidoId);

            if (pedido == null) {
                return ResponseEntity.badRequest().body("Pedido no encontrado");
            }

            // Validación mejorada: verificar si pedido.getEnvio() es null antes de acceder
            if (pedido.getEnvio() != null) {
                return ResponseEntity.badRequest().body("Este pedido ya ha sido asignado a un envío");
            }

            Envio nuevoEnvio = envioService.asignarPedidoAEnvio(pedidoId, ruta);

            // Asegurar que el envío queda registrado en Pedido
            pedido.setEnvio(nuevoEnvio);

            return ResponseEntity.ok(nuevoEnvio);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error inesperado al asignar pedido a envío: " + e.getMessage());
        }
    }


    @PutMapping("/actualizar-estado/{envioId}")
    public ResponseEntity<Envio> actualizarEstadoEntrega(@PathVariable Long envioId,
                                                         @RequestParam String estado) {
        try {
            Envio envioActualizado = envioService.actualizarEstadoEntrega(envioId, estado);
            return ResponseEntity.ok(envioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/por-cliente")
    public List<Envio> obtenerEnviosPorCliente(@RequestParam int clienteId) {
        return envioService.obtenerPorCliente(clienteId);
    }

}
