package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.*;
import com.example.SpringSemestral.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private CuponRepository cuponRepository;
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    @Autowired
    private EnvioRepository envioRepository;
    public String crearPedido(Pedido pedido, String cuponCodigo) {
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("PENDIENTE");

        double total = pedido.getDetalles().stream()
                .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum();

        if (cuponCodigo != null) {
            Cupon cupon = cuponRepository.findByCodigo(cuponCodigo).orElse(null);
            if (cupon != null) {
                double descuento = total * (cupon.getPorcentajeDescuento() / 100.0);
                total -= descuento;
            }
        }

        pedido.getDetalles().forEach(d -> d.setPedido(pedido));
        pedidoRepository.save(pedido);

        return "Pedido creado con total: $" + total;
    }


    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPedidosPorCliente(int clienteId) {
        return pedidoRepository.findByCliente_Id(clienteId);
    }
    public Pedido getPedidoById(Integer pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
    }

    public String confirmarPago(int pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) return "Pedido no encontrado";

        if (!"PAGADO".equalsIgnoreCase(pedido.getEstado())) {
            pedido.setEstado("PAGADO");

            // Calcular monto total
            double total = pedido.getDetalles().stream()
                    .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                    .sum();

            // Crear factura
            Factura factura = new Factura();
            factura.setFechaEmision(LocalDate.now());
            factura.setMontoTotal(total);
            factura.setPedido(pedido);
            factura.setCliente(pedido.getCliente());

            // Asociar factura al pedido (relación bidireccional)
            pedido.setFactura(factura);

            // Guardar ambos
            facturaRepository.save(factura);
            pedidoRepository.save(pedido);

            return "Pago confirmado y factura generada";
        }

        return "El pedido ya está pagado";
    }
    @Transactional
    public String eliminarPedido(int id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        if (!pedido.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return "⚠️ El pedido que desea eliminar ya fue procesado. Solo los pedidos con estado PENDIENTE pueden cancelarse o eliminarse.";
        }

        // Eliminar factura si existe
        if (pedido.getFactura() != null) {
            facturaRepository.delete(pedido.getFactura());
        }

        // Eliminar detalles si hay
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            detallePedidoRepository.deleteAll(pedido.getDetalles());
        }

        // Eliminar envío si existe
        if (pedido.getEnvio() != null) {
            envioRepository.delete(pedido.getEnvio());
        }

        pedidoRepository.delete(pedido);
        return "✅ Pedido eliminado correctamente.";
    }

    public String cancelarPedido(int id) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido == null) return "Pedido no encontrado";

        if (!pedido.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return "Solo se pueden cancelar pedidos pendientes";
        }

        pedido.setEstado("CANCELADO");
        pedidoRepository.save(pedido);
        return "Pedido cancelado correctamente";
    }

}