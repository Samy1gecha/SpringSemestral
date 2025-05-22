package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Envio;
import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Repository.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private PedidoService pedidoService; // Para buscar pedidos

    public Envio asignarPedidoAEnvio(Integer pedidoId, String ruta) {
        System.out.println("Buscando pedido con id " + pedidoId);

        Pedido pedido = pedidoService.getPedidoById(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado");
        }

        System.out.println("Pedido encontrado: " + pedido);

        Envio envio = new Envio();
        envio.setPedido(pedido);
        envio.setEstado("Pendiente");
        envio.setRuta(ruta);
        envio.setFechaAsignacion(LocalDateTime.now());

        System.out.println("Guardando envio...");

        try {
            return envioRepository.save(envio);
        } catch (Exception e) {
            e.printStackTrace(); // Ver logs para detalle del error
            throw new RuntimeException("Error al guardar el envío");
        }
    }


    public Envio actualizarEstadoEntrega(Long envioId, String nuevoEstado) {
        Optional<Envio> optionalEnvio = envioRepository.findById(envioId);
        if (optionalEnvio.isEmpty()) {
            throw new IllegalArgumentException("Envío no encontrado");
        }
        Envio envio = optionalEnvio.get();
        envio.setEstado(nuevoEstado);
        return envioRepository.save(envio);
    }
    public List<Envio> listarEnvios() {
        return envioRepository.findAll();
    }

    public List<Envio> obtenerPorCliente(int clienteId) {
        try {
            System.out.println("Buscando envíos para cliente ID: " + clienteId);
            List<Envio> envios = envioRepository.findByPedido_Cliente_Id(clienteId);
            System.out.println("Cantidad de envíos encontrados: " + envios.size());
            return envios;
        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá el error en consola
            return new ArrayList<>();
        }
    }
}

