package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")

public class Controller {
    @Autowired
    UserService userService;

    @GetMapping
    public List<User> getUsers() {return userService.getAllUsers();}

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {return userService.getUser(id);}

    @PostMapping
    public String postUser(@RequestBody User user){return userService.addUser(user);}

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id){
        return userService.deleteUser(id);
    }
    @PutMapping
    public String putUser(@RequestBody User user){
        return userService.updateUser(user);
    }
    @GetMapping("/email/{email}") //Para el userService por email
    public User getUserByEmail(@PathVariable String email) {return userService.getUserByEmail(email);}

}
