package com.example.SpringSemestral.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String username;
    private String password;
    @Column(unique = true)  // <-- Aquí el email será único en BD
    private String email;
    @Enumerated(EnumType.STRING)
    private Rol rol;

    public enum Rol {
        ADMIN,
        CLIENTE,
        EMPLEADO
    }
}