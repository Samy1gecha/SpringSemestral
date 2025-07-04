package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Model.Reclamo;
import com.example.SpringSemestral.Controlador.ReclamoController;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReclamoModelAssembler
        implements RepresentationModelAssembler<Reclamo, EntityModel<Reclamo>> {

    @Override
    public EntityModel<Reclamo> toModel(Reclamo reclamo) {
        return EntityModel.of(reclamo,
                // enlace a este reclamo
                linkTo(methodOn(ReclamoController.class).obtenerPorId(reclamo.getId())).withSelfRel(),
                // enlace al listado de todos los reclamos
                linkTo(methodOn(ReclamoController.class).obtenerTodos()).withRel("reclamos"),
                // enlace a reclamos de este cliente
                linkTo(methodOn(ReclamoController.class).obtenerPorCliente(reclamo.getCliente().getId())).withRel("reclamos-por-cliente"),
                // enlace para cambiar el estado
                linkTo(methodOn(ReclamoController.class).cambiarEstado(reclamo.getId(), null)).withRel("cambiar-estado"),
                // enlace para eliminar
                linkTo(methodOn(ReclamoController.class).eliminarReclamo(reclamo.getId())).withRel("eliminar")
        );
    }
}

