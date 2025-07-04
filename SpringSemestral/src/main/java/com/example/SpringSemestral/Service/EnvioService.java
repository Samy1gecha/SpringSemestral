package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Envio;
import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Repository.EnvioRepository;
import com.example.SpringSemestral.Repository.PedidoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EnvioService {
    @Autowired
    private EnvioRepository envioRepository;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private PedidoRepository pedidoRepository;

    // Asigna un pedido a un nuevo envío
    @Transactional
    public Envio asignarPedidoAEnvio(Integer pedidoId, String ruta) {
        System.out.println("Buscando pedido con id " + pedidoId);
        Pedido pedido = pedidoService.getPedidoById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }
        System.out.println("Pedido encontrado: " + pedido);
        if (pedido.getEnvio() != null) {
            throw new IllegalStateException("Este pedido ya ha sido asignado a un envío");
        }

        Envio envio = new Envio();
        envio.setPedido(pedido);
        envio.setEstado("Pendiente");
        envio.setRuta(ruta);
        envio.setFechaAsignacion(LocalDateTime.now());

        System.out.println("Guardando envio...");
        try {
            Envio saved = envioRepository.save(envio);
            // Asegurar que el pedido referencie el envío recién creado
            pedido.setEnvio(saved);
            pedidoRepository.save(pedido);
            return saved;
        } catch (Exception e) {
            e.printStackTrace(); // para ver detalles en logs
            throw new RuntimeException("Error al guardar el envío");
        }
    }
    // Actualiza el estado de un envío y, si es Entregado, actualiza el pedido
    // Actualiza el estado de un envío y marca el pedido como entregado si corresponde
    @Transactional
    public Envio actualizarEstadoEntrega(Long envioId, String nuevoEstado) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new NoSuchElementException("Envío no encontrado"));
        envio.setEstado(nuevoEstado);
        if ("Entregado".equalsIgnoreCase(nuevoEstado)) {
            Pedido pedido = envio.getPedido();
            pedido.setEstado("Entregado");
            pedidoRepository.save(pedido);
        }
        return envioRepository.save(envio);
    }
    // Devuelve todos los envíos registrados
    public List<Envio> listarEnvios() {
        return envioRepository.findAll();
    }
    // Recupera envíos por ID de cliente
    public List<Envio> obtenerPorCliente(int clienteId) {
        return envioRepository.findByPedido_Cliente_Id(clienteId);
    }
    // Obtiene un envío opcional por su ID
    public Optional<Envio> getEnvioById(Long id) {
        return envioRepository.findById(id);
    }
    public Optional<Envio> getByPedidoId(int pedidoId) {
        return envioRepository.findByPedido_Id(pedidoId);
    }
    // Elimina un envío existente
    @Transactional
    public void eliminarEnvio(Long id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Envío no encontrado"));
        envioRepository.delete(envio);
    }
}


