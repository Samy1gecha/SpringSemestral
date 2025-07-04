package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Controlador.ReporteController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReporteModelAssembler
        implements RepresentationModelAssembler<Double, EntityModel<Double>> {

    @Override
    public EntityModel<Double> toModel(Double total) {
        return EntityModel.of(total,
                linkTo(methodOn(ReporteController.class).getTotalVentas()).withSelfRel(),
                linkTo(methodOn(ReporteController.class).getProductosMasVendidos()).withRel("productos-mas-vendidos"),
                linkTo(methodOn(ReporteController.class).getTotalPorCliente()).withRel("total-por-cliente"),
                linkTo(methodOn(ReporteController.class).getTotalVentasPorFecha(null, null)).withRel("ventas-por-fecha")
        );
    }
}


