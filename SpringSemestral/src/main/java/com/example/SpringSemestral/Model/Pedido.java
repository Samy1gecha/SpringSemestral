package com.example.SpringSemestral.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PEDIDOS")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate fecha;

    private String estado; // ejemplo: "PENDIENTE", "ENTREGADO"

    @JsonIgnoreProperties(value = { "password" }) // Esto oculta solo la contraseña del cliente en la respuesta JSON
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private User cliente;
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Envio envio;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Factura factura;

}
