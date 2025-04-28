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

    /**
     * Inicia sesión de un usuario verificando su nombre de usuario y contraseña.
     * @param username Nombre de usuario del usuario que desea iniciar sesión
     * @param password Contraseña del usuario
     * @return Mensaje de éxito o error
     */
    public String loginUsuario(String username, String password) {
        // Buscar el usuario en el repositorio y verificar sus credenciales
        for (User user : userRepository.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                return "Login exitoso";
            }
        }
        return "Usuario o contraseña incorrectos";
    }

    //Actualiza los datos de un usuario en el repositorio.
    //Reemplaza el usuario por el otro
    public void actualizarUsuario(User user) {
        // Actualizar el usuario en el repositorio
        userRepository.updateUser(user);
    }

    //Elimina un usuario del repositorio por su nombre de usuario
    public void eliminarUsuario(String username) {
        // Eliminar el usuario del repositorio
        userRepository.removeUser(username);
    }
}
