package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Factura;
import com.example.SpringSemestral.Model.Resena;
import com.example.SpringSemestral.Repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Repository.PedidoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Factura> verTodas() {
        return facturaRepository.findAll();
    }
    public List<Factura> buscarPorCliente(int clienteId) {
        return facturaRepository.findByCliente_Id(clienteId);
    }

    public List<Factura> buscarPorFecha(LocalDate desde, LocalDate hasta) {
        return facturaRepository.findByFechaEmisionBetween(desde, hasta);
    }
    @Transactional
    public String confirmarPago(int pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado."));

        if (!pedido.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return "⚠️ Solo se pueden confirmar pagos de pedidos en estado PENDIENTE.";
        }

        // Calcular el monto total
        double montoTotal = pedido.getDetalles().stream()
                .mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario())
                .sum();

        // Crear factura
        Factura factura = new Factura();
        factura.setFechaEmision(LocalDate.now());
        factura.setEstado("Emitida");
        factura.setCliente(pedido.getCliente());
        factura.setPedido(pedido);
        factura.setMontoTotal(montoTotal);
        facturaRepository.save(factura);
        // Guardar factura y actualizar pedido

        pedido.setEstado("PAGADO");
        pedido.setFactura(factura);
        pedidoRepository.save(pedido);

        return "✅ Pago confirmado y factura generada con éxito.";
    }


}

