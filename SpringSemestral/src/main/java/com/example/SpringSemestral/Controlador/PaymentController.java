package com.example.SpringSemestral.Controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pagos")
public class PaymentController {

    @PostMapping("/simular")
    public ResponseEntity<String> simularPago(@RequestBody Map<String, Object> pagoInfo) {
        // Aquí puedes validar datos mínimos, como monto y método de pago
        if (!pagoInfo.containsKey("monto") || !pagoInfo.containsKey("metodoPago")) {
            return ResponseEntity.badRequest().body("Faltan datos obligatorios: monto o metodoPago");
        }

        Double monto = Double.valueOf(pagoInfo.get("monto").toString());
        String metodoPago = pagoInfo.get("metodoPago").toString();

        // Simulación simple: aprobamos pagos menores a 1000, rechazamos mayores
        if (monto <= 1000) {
            return ResponseEntity.ok("Pago simulado exitoso con método: " + metodoPago + ", monto: " + monto);
        } else {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                    .body("Pago simulado rechazado por monto demasiado alto: " + monto);
        }
    }
}
