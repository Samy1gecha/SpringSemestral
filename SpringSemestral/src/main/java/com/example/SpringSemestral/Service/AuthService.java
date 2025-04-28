package Service;

import Modelo.User;
import Repositorio.UserRepository;

public class AuthService {

    private UserRepository userRepository;

    // Constructor que recibe el UserRepository para poder consultar usuarios
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Método para autenticar un usuario (login)
     * @param username nombre de usuario
     * @param password contraseña ingresada
     * @return mensaje indicando éxito o error
     */
    public String login(String username, String password) {
        // Buscar el usuario en el repositorio
        for (User user : userRepository.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                // Verificar si las contraseñas coinciden (por ejemplo con BCrypt)
                if (user.getPassword().equals(password)) {
                    return "Login exitoso";
                }
                return "Contraseña incorrecta";
            }
        }
        return "Usuario no encontrado";
    }

    /**
     * Método para cerrar sesión (logout)
     * @param username nombre de usuario que quiere cerrar sesión
     * @return mensaje de logout
     */
    public String logout(String username) {
        //Como estamos en una versión básica (sin sesiones reales), solo devolvemos un mensaje
        //Uso de BCrypt para contraseñas seguras.
        return "Logout exitoso para el usuario: " + username;
    }
}
