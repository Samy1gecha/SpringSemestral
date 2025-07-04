package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Controlador.PedidoController;
import com.example.SpringSemestral.Controlador.ResenaController;
import com.example.SpringSemestral.Controlador.UserController;
import com.example.SpringSemestral.Model.Product;
import com.example.SpringSemestral.Controlador.ProductController;

import com.example.SpringSemestral.Model.Resena;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public EntityModel<Product> toModel(Product product) {
        return EntityModel.of(product,
                linkTo(methodOn(ProductController.class).getProductById(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getProducts()).withRel("productos"),
                linkTo(methodOn(ResenaController.class).verTodas()).withRel("resenas"),
                linkTo(methodOn(ProductController.class).putProduct(product, "ADMIN")).withRel("editar"),
                linkTo(methodOn(ResenaController.class).crear(new Resena())).withRel("publicar-resena")
        );
    }
}
