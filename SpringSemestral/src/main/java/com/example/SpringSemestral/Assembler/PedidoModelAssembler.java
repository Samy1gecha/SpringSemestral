package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Model.Pedido;
import com.example.SpringSemestral.Controlador.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    public EntityModel<Pedido> toModel(Pedido pedido) {
        EntityModel<Pedido> model = EntityModel.of(pedido,
                // Enlace al pedido individual
                linkTo(methodOn(PedidoController.class).obtenerPedido(pedido.getId())).withSelfRel(),
                // Enlace a todos los pedidos
                linkTo(methodOn(PedidoController.class).listarTodos()).withRel("pedidos"),
                // Enlace al cliente asociado
                linkTo(methodOn(UserController.class).getUserById(pedido.getCliente().getId())).withRel("cliente"),
                // Enlace a factura (si la tiene)
                linkTo(methodOn(FacturaController.class).getFacturaById(pedido.getId())).withRel("factura"),
                // Enlace a envío (si lo tiene)
                linkTo(methodOn(EnvioController.class).verPorPedido(pedido.getId())).withRel("envio"),
                // Enlace a seguimiento
                linkTo(methodOn(PedidoController.class).seguimiento(pedido.getId())).withRel("seguimiento"),
                // Enlace para confirmar pago
                linkTo(methodOn(PedidoController.class).confirmarPago(pedido.getId())).withRel("confirmar-pago"),
                // Enlace para cancelar
                linkTo(methodOn(PedidoController.class).cancelarPedido(pedido.getId())).withRel("cancelar")
        );
        return model;
    }
}

