package com.example.SpringSemestral.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DETALLE_PEDIDO")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int cantidad;
    private double precioUnitario;

    @JsonIgnore //Esto oculta completamente el campo al serializar
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product producto;
}
