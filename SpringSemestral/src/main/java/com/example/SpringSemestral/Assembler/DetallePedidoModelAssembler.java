package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Model.DetallePedido;
import com.example.SpringSemestral.Controlador.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DetallePedidoModelAssembler implements RepresentationModelAssembler<DetallePedido, EntityModel<DetallePedido>> {

    @Override
    public EntityModel<DetallePedido> toModel(DetallePedido detalle) {
        return EntityModel.of(detalle,
                // Enlace al producto relacionado
                linkTo(methodOn(ProductController.class).getProductById(detalle.getProducto().getId())).withRel("producto"),
                // Enlace al pedido relacionado
                linkTo(methodOn(PedidoController.class).obtenerPedido(detalle.getPedido().getId())).withRel("pedido"),
                // Enlace a todos los detalles del pedido
                linkTo(methodOn(DetallePedidoController.class).listarPorPedido(detalle.getPedido().getId())).withRel("detalles-del-pedido")
        );
    }
}
