package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Model.Factura;
import com.example.SpringSemestral.Controlador.FacturaController;
import com.example.SpringSemestral.Controlador.PedidoController;
import com.example.SpringSemestral.Controlador.UserController;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class FacturaModelAssembler
        implements RepresentationModelAssembler<Factura, EntityModel<Factura>> {

    @Override
    public EntityModel<Factura> toModel(Factura factura) {
        return EntityModel.of(factura,
                // enlace a esta factura
                linkTo(methodOn(FacturaController.class).getFacturaById(factura.getId())).withSelfRel(),
                // enlace al listado de todas las facturas
                linkTo(methodOn(FacturaController.class).getAllFacturas()).withRel("facturas"),
                // enlace al pedido al que pertenece
                linkTo(methodOn(PedidoController.class).obtenerPedido(factura.getPedido().getId())).withRel("pedido"),
                // enlace al cliente destinatario de la factura
                linkTo(methodOn(UserController.class).getUserById(factura.getCliente().getId())).withRel("cliente")
        );
    }
}
