package com.example.SpringSemestral.Model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RECLAMOS")
public class Reclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String motivo;

    private String estado; // PENDIENTE, ACEPTADO, RECHAZADO

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private User cliente;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
