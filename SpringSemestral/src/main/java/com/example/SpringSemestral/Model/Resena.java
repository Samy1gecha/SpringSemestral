package com.example.SpringSemestral.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RESENAS")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String comentario;
    private int calificacion;
    @JsonProperty("cliente")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties(value = { "password", "email", "rol" }) // mostramos solo username
    private User cliente;

    @JsonProperty("producto")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    @JsonIgnoreProperties(value = { "descripcion", "precio", "stock" }) // ocultamos lo innecesario
    private Product product;

}
