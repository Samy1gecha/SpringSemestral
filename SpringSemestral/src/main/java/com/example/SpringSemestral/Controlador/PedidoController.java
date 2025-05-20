package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public String crear(@RequestBody Pedido pedido) {
        return pedidoService.crearPedido(pedido);
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPorCliente(@PathVariable int clienteId) {
        return pedidoService.listarPedidosPorCliente(clienteId);
    }
}
