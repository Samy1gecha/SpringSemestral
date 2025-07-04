package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Service.UserService;
import com.example.SpringSemestral.Assembler.UserModelAssembler;
import com.example.SpringSemestral.Repository.PedidoRepository;
import com.example.SpringSemestral.Repository.ReclamoRepository;
import com.example.SpringSemestral.Repository.ResenaRepository;
import com.example.SpringSemestral.Repository.UserRepository;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ReclamoRepository reclamoRepository;
    @Autowired private ResenaRepository resenaRepository;
    @Autowired private UserModelAssembler assembler;

    @Operation(summary = "Obtener todos los usuarios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados"),
            @ApiResponse(responseCode = "404", description = "No hay usuarios registrados")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<User>>> getAllUsers() {
        List<User> lista = userService.getAllUsers();
        if (lista.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("Mensaje :", "❌ No hay usuarios registrados.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(assembler.toCollectionModel(lista));
    }

    @Operation(summary = "Obtener usuario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUserById(@PathVariable int id) {
        return userService.getOptionalUser(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Registrar nuevo usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<User>> addUser(@RequestBody User user) {
        userService.addUser(user);
        return userService.getOptionalUser(user.getId())
                .map(assembler::toModel)
                .map(model -> ResponseEntity.status(HttpStatus.CREATED).body(model))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(summary = "Eliminar usuario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id) {
        try {
            String result = userService.deleteUser(id);
            return ResponseEntity.ok(result);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @Operation(summary = "Actualizar usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> updateUser(@PathVariable int id, @RequestBody User user) {
        return userService.getOptionalUser(id)
                .map(existing -> {
                    userService.updateUser(id, user);
                    return ResponseEntity.ok(assembler.toModel(user));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Buscar usuario por email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con ese email")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<EntityModel<User>> getUserByEmail(@PathVariable String email) {
        return userService.getOptionalUserByEmail(email)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

