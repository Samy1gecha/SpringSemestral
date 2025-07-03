    package com.example.SpringSemestral.Model;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

    import com.fasterxml.jackson.annotation.JsonManagedReference;
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
        @Column(name = "fecha_emision")
        private LocalDate fechaEmision;
        private String estado;

        @ManyToOne
        @JoinColumn(name = "cliente_id")
        @JsonIgnoreProperties({"reclamos", "reseñas", "password"})
        private User cliente;

        private double montoTotal;
        @OneToOne
        @JoinColumn(name = "pedido_id")
        @JsonManagedReference
        private Pedido pedido;
    }

