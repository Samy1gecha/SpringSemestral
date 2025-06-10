package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Repository.PedidoRepository;
import com.example.SpringSemestral.Repository.ReclamoRepository;
import com.example.SpringSemestral.Repository.ResenaRepository;
import com.example.SpringSemestral.Repository.UserRepository;
import com.example.SpringSemestral.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")

public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ReclamoRepository reclamoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResenaRepository resenaRepository;
    @GetMapping
    public List<User> getUsers() {return userService.getAllUsers();}

    @GetMapping("/{id}")

    public User getUserById(@PathVariable int id) {return userService.getUser(id);}

    @PostMapping
    public String postUser(@RequestBody User user){return userService.addUser(user);}

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "Usuario no encontrado";

        // Eliminar primero todas las entidades relacionadas
        pedidoRepository.deleteAll(pedidoRepository.findByCliente_Id(id));
        reclamoRepository.deleteAll(reclamoRepository.findByCliente_Id(id));
        resenaRepository.deleteAll(resenaRepository.findByCliente_Id(id));

        // Ahora se puede eliminar el usuario
        userRepository.deleteById(id);
        return "Usuario eliminado con éxito";
    }

    @PutMapping
    public String putUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @GetMapping("/email/{email}") //Para el userService por email
    public User getUserByEmail(@PathVariable String email) {return userService.getUserByEmail(email);}

}
