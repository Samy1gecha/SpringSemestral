package Controlador;

import Modelo.User;
import Repositorio.UserRepository;

public class UserController {
    // Instancia del repositorio de usuarios para interactuar con los datos
    private UserRepository userRepository = new UserRepository();

    /**
     * Registra un nuevo usuario en el sistema.
     * @param username Nombre de usuario para el nuevo usuario
     * @param password Contraseña del nuevo usuario
     * @param gmail Correo electrónico del nuevo usuario
     */
    public void registrarUsuario(String username, String password, String gmail, String role) {
        // Crear un nuevo objeto User y agregarlo al repositorio
        User user = new User(username, password, gmail, role);
        userRepository.addUser(user);
    }

    //Actualiza los datos de un usuario en el repositorio.
    //Reemplaza el usuario por el otro
    public void actualizarUsuario(User user) {
        // Actualizar el usuario en el repositorio
        userRepository.updateUser(user);
    }
    // Método para obtener usuarios por rol
    public List<User> obtenerUsuariosPorRol(String role) {
        return userRepository.getUsersByRole(role);
    }
    // Método para actualizar el rol de un usuario
    public void actualizarRolUsuario(String username, String newRole) {
        userRepository.updateUserRole(username, newRole);
    }
    //Elimina un usuario del repositorio por su nombre de usuario
    public void eliminarUsuario(String username) {
        // Eliminar el usuario del repositorio
        userRepository.removeUser(username);
    }
}
