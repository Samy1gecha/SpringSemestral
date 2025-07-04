package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Model.Envio;
import com.example.SpringSemestral.Controlador.EnvioController;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EnvioModelAssembler
        implements RepresentationModelAssembler<Envio, EntityModel<Envio>> {

    @Override
    public EntityModel<Envio> toModel(Envio envio) {
        return EntityModel.of(envio,
                // enlace a este envío
                linkTo(methodOn(EnvioController.class).obtenerEnvioPorId(envio.getId())).withSelfRel(),
                // enlace al listado de todos los envíos
                linkTo(methodOn(EnvioController.class).listarEnvios()).withRel("envios"),
                // enlace al pedido asociado a este envío
                linkTo(methodOn(EnvioController.class).verPorPedido(envio.getPedido().getId())).withRel("pedido"),
                // enlace para actualizar estado de entrega
                linkTo(methodOn(EnvioController.class).actualizarEstadoEntrega(envio.getId(), null)).withRel("actualizar-estado"),
                // enlace para obtener envíos por cliente (usa el cliente del pedido)
                linkTo(methodOn(EnvioController.class).obtenerEnviosPorCliente(envio.getPedido().getCliente().getId())).withRel("envios-por-cliente")
        );
    }
}
