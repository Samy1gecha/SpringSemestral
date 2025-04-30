package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.User;
import com.example.SpringSemestral.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")

public class Controller {
    @Autowired
    UserService userService;

    @GetMapping
    public String getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable int id){
        return userService.getUser(id);
    }

    @PostMapping
    public String postUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id){
        return userService.deleteUser(id);
    }
    @PutMapping
    public String putUser(@RequestBody User user){
        return userService.updateUser(user);
    }
}
