package com.example.SpringSemestral.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private User cliente;

    private double montoTotal;
    @JsonIgnoreProperties({"factura"}) // evitar que el pedido incluya la factura de vuelta
    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}

