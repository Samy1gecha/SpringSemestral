package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Controlador.CuponController;
import com.example.SpringSemestral.Model.Cupon;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CuponModelAssembler
        implements RepresentationModelAssembler<Cupon, EntityModel<Cupon>> {

    @Override
    public EntityModel<Cupon> toModel(Cupon c) {
        return EntityModel.of(c,
                linkTo(methodOn(CuponController.class).getById(c.getId())).withSelfRel(),
                linkTo(methodOn(CuponController.class).getAll()).withRel("cupones"),
                linkTo(methodOn(CuponController.class).deleteById(c.getId())).withRel("eliminar")
        );
    }
}
