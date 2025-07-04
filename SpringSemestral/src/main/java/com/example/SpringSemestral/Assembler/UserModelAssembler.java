package com.example.SpringSemestral.Assembler;

import com.example.SpringSemestral.Controlador.*;
import com.example.SpringSemestral.Model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        EntityModel<User> model = EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("usuarios"),
                linkTo(methodOn(PedidoController.class).getPedidosByClienteId(user.getId())).withRel("pedidos"),
                linkTo(methodOn(ResenaController.class).verPorCliente(user.getId())).withRel("resenas"),
                linkTo(methodOn(UserController.class).getUserByEmail(user.getEmail())).withRel("buscar-por-email"),
                linkTo(methodOn(UserController.class).updateUser(user.getId(), user)).withRel("editar-perfil"),
                linkTo(methodOn(UserController.class).deleteUserById(user.getId())).withRel("eliminar")
        );
        return model;
    }
}

