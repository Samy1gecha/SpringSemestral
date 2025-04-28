package Modelo;

public class User {
    private String username;
    private String password;
    private String gmail;
    private String role; // Añadido para manejar roles (Admin, Cliente, etc.)

    public User(){

    }

    public User(String username, String password, String gmail, String role) {
        this.username = username;
        this.password = password;
        this.gmail = gmail;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public void setRole(String role) {this.role = role;}

    public String getRole() {return role;}

    @Override
    public String toString(){
        String output="";
        output+= "Nombre: "+username+"\n";
        output+= "Contraseña: "+password+"\n";
        output+= "Gmail: "+gmail+"\n";
        return "Username: " + username + ", Email: " + gmail + ", Role: " + role;
    }
}


