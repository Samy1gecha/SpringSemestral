package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Controlador.ResenaController;
import com.example.SpringSemestral.Model.Resena;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ResenaModelAssembler implements RepresentationModelAssembler<Resena, EntityModel<Resena>> {
    @Override
    public EntityModel<Resena> toModel(Resena resena) {
        return EntityModel.of(resena,
                linkTo(methodOn(ResenaController.class).verPorId(resena.getId())).withSelfRel(),
                linkTo(methodOn(ResenaController.class).verTodas()).withRel("resenas"),
                linkTo(methodOn(ResenaController.class).eliminarPorId(resena.getId())).withRel("eliminar"),
                linkTo(methodOn(ResenaController.class).obtenerPorProducto(resena.getProduct().getId())).withRel("por-producto"),
                linkTo(methodOn(ResenaController.class).verPorCliente(resena.getCliente().getId())).withRel("por-cliente")
        );
    }
}