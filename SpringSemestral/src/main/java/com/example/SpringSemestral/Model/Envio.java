package com.example.SpringSemestral.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnoreProperties("envio") // Evita bucles infinitos si Pedido tiene relación a Envio
    @JsonBackReference
    private Pedido pedido;

    private String estado; // Ej: "Pendiente", "Enviado", "Entregado"

    private String ruta; // Ruta optimizada (texto simple)

    private LocalDateTime fechaAsignacion;
}

