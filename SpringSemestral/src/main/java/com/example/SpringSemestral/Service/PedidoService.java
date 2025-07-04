package com.example.SpringSemestral.Service;
import com.example.SpringSemestral.Model.*;
import com.example.SpringSemestral.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private FacturaRepository facturaRepository;
    @Autowired private CuponRepository cuponRepository;
    @Autowired private DetallePedidoRepository detallePedidoRepository;
    @Autowired private EnvioRepository envioRepository;

    public double crearPedido(Pedido pedido, String cuponCodigo) {
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("PENDIENTE");

        final double[] total = {pedido.getDetalles().stream()
                .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum()};
        if (cuponCodigo != null) {
            cuponRepository.findByCodigo(cuponCodigo).ifPresent(cupon -> {
                double descuento = total[0] * (cupon.getPorcentajeDescuento() / 100.0);
                total[0] -= descuento;
            });
        }
        pedido.getDetalles().forEach(detalle -> detalle.setPedido(pedido));
        pedidoRepository.save(pedido);
        return total[0];
    }
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }
    public List<Pedido> listarPedidosPorCliente(int clienteId) {
        return pedidoRepository.findByCliente_Id(clienteId);
    }
    public Pedido getPedidoById(int id) {
        return pedidoRepository.findById(id).orElse(null);
    }
    public Optional<Pedido> getOptionalPedido(int pedidoId) {
        return pedidoRepository.findById(pedidoId);
    }
    public void confirmarPago(int pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado"));
        if ("PAGADO".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalStateException("El pedido ya está pagado.");
        }
        pedido.setEstado("PAGADO");
        double total = pedido.getDetalles().stream()
                .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum();
        Factura factura = new Factura();
        factura.setFechaEmision(LocalDate.now());
        factura.setMontoTotal(total);
        factura.setPedido(pedido);
        factura.setCliente(pedido.getCliente());
        pedido.setFactura(factura);
        facturaRepository.save(factura);
        pedidoRepository.save(pedido);
    }
    @Transactional
    public void eliminarPedido(int id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado"));
        if (!"PENDIENTE".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalStateException("⚠️ Solo se pueden eliminar pedidos con estado PENDIENTE.");
        }
        if (pedido.getFactura() != null) {
            facturaRepository.delete(pedido.getFactura());
        }
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            detallePedidoRepository.deleteAll(pedido.getDetalles());
        }
        if (pedido.getEnvio() != null) {
            envioRepository.delete(pedido.getEnvio());
        }
        pedidoRepository.delete(pedido);
    }
    public void cancelarPedido(int id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado"));

        if (!"PENDIENTE".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalStateException("Solo se pueden cancelar pedidos pendientes.");
        }
        pedido.setEstado("CANCELADO");
        pedidoRepository.save(pedido);
    }
}
