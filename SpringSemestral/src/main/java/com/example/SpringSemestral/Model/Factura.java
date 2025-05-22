package com.example.SpringSemestral.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FACTURAS")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate fechaEmision;

    private double montoTotal;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}

