package Repository;

import Modelo.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users = new ArrayList<>();

    public UserRepository() {
        // Constructor vacío
    }
    //Busca un usuario por su nombre de usuario y devuelve su información como String.
    public String getUser(String username){
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user.toString();
            }
        }
        return "Usuario no encontrado";
    }
    //Agrega un nuevo usuario a la lista
    public void addUser(User user){
        users.add(user);
        System.out.println("Usuario agregado con éxito");
    }
    //Elimina un usuario de la lista
    public void removeUser(String username){
        boolean removed = false;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                users.remove(user);
                System.out.println("Usuario eliminado con éxito");
                removed = true;
                break;
            }
        }
        if (!removed) {
            System.out.println("Usuario no encontrado");
        }
    }
    //Actualiza la informacion de un usuario existente
    public void updateUser(User newUser){
        boolean updated = false;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(newUser.getUsername())) {
                int index = users.indexOf(user);
                users.set(index, newUser);
                System.out.println("Usuario actualizado con éxito");
                updated = true;
                break;
            }
        }
        if (!updated) {
            System.out.println("Usuario no encontrado");
        }
    }
    //Devuelve una representacion de todos los usuarios creados
    public String getUsers(){
        StringBuilder output = new StringBuilder();
        for (User user : users) {
            output.append(user.toString()).append("\n");
        }
        return output.toString();
    }
}
