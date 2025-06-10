package com.example.SpringSemestral.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Envio  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnoreProperties // oculta el campo id en JSON
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnoreProperties({"envio"})  // Evita ciclo serializando Pedido -> Envio -> Pedido
    @JsonIgnore
    private Pedido pedido;

    private String estado; // Ej: "Pendiente", "Enviado", "Entregado"

    private String ruta;

    private LocalDateTime fechaAsignacion;
}
