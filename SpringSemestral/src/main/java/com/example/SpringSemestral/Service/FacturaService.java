package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Factura;
import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Repository.FacturaRepository;
import com.example.SpringSemestral.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FacturaService {
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    //Recupera todas las facturas.
    public List<Factura> verTodas() {
        return facturaRepository.findAll();
    }
    //Trae todas las facturas
    public List<Factura> getAllFacturas() {
        return facturaRepository.findAll();
    }
    public Optional<Factura> getByPedidoId(int pedidoId) {
        return facturaRepository.findByPedido_Id(pedidoId);
    }
    //Busca una factura por su ID.
    public Optional<Factura> getOptionalFactura(int id) {
        return facturaRepository.findById(id);
    }
    //Verifica si existe una factura con el ID dado.

    public boolean existsById(int id) {
        return facturaRepository.existsById(id);
    }
    //Recupera todas las facturas de un cliente.

    public List<Factura> buscarPorCliente(int clienteId) {
        return facturaRepository.findByCliente_Id(clienteId);
    }
    //Recupera todas las facturas emitidas en un rango de fechas.

    public List<Factura> buscarPorFecha(LocalDate desde, LocalDate hasta) {
        return facturaRepository.findByFechaEmisionBetween(desde, hasta);
    }
    /**
     * Confirma el pago de un pedido y genera la factura correspondiente.
     * @param pedidoId el ID del pedido a facturar
     * @return mensaje de éxito o advertencia
     * @throws IllegalArgumentException si el pedido no existe
     */
    @Transactional
    public String confirmarPago(int pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado."));

        if (!"PENDIENTE".equalsIgnoreCase(pedido.getEstado())) {
            return "⚠️ Solo se pueden confirmar pagos de pedidos en estado PENDIENTE.";
        }

        // Calcular el monto total
        double montoTotal = pedido.getDetalles().stream()
                .mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario())
                .sum();
        // Crear y guardar factura
        Factura factura = new Factura();
        factura.setFechaEmision(LocalDate.now());
        factura.setEstado("Emitida");
        factura.setCliente(pedido.getCliente());
        factura.setPedido(pedido);
        factura.setMontoTotal(montoTotal);
        facturaRepository.save(factura);

        // Actualizar estado del pedido
        pedido.setEstado("PAGADO");
        pedido.setFactura(factura);
        pedidoRepository.save(pedido);

        return "✅ Pago confirmado y factura generada con éxito.";
    }
    /**
     * Cambia el estado de una factura.
     *
     * @param id          el ID de la factura a actualizar
     * @param nuevoEstado el nuevo estado ("Emitida", "Pagada" o "Anulada")
     * @throws NoSuchElementException   si no existe la factura
     * @throws IllegalArgumentException si el estado no es válido
     */
    public void cambiarEstado(int id, String nuevoEstado) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Factura no encontrada"));
        var validos = List.of("Emitida", "Pagada", "Anulada");
        if (!validos.contains(nuevoEstado)) {
            throw new IllegalArgumentException(
                    "Estado inválido. Opciones válidas: " + String.join(", ", validos)
            );
        }
        factura.setEstado(nuevoEstado);
        facturaRepository.save(factura);
    }
}