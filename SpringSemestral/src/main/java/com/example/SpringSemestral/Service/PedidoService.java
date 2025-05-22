package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Pedido;
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

    public String crearPedido(Pedido pedido) {
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("PENDIENTE");
        pedido.getDetalles().forEach(d -> d.setPedido(pedido)); // vincula detalles al pedido
        pedidoRepository.save(pedido);
        return "Pedido creado con éxito";
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPedidosPorCliente(int clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
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