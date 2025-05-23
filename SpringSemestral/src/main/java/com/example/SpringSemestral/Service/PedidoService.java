package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Cupon;
import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Repository.CuponRepository;
import com.example.SpringSemestral.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.SpringSemestral.Model.Factura;
import com.example.SpringSemestral.Repository.FacturaRepository;

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
    public String cancelarPedido(int pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null) return "Pedido no encontrado";

        if (!pedido.getEstado().equalsIgnoreCase("PENDIENTE")) {
            return "Solo se pueden cancelar pedidos pendientes";
        }

        pedido.setEstado("CANCELADO");
        pedidoRepository.save(pedido);
        return "Pedido cancelado correctamente";
    }
    public List<Pedido> obtenerPedidosPorCliente(int clienteId) {
        return pedidoRepository.findByCliente_Id(clienteId);
    }

}