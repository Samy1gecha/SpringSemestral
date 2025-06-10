package com.example.SpringSemestral.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUPONES")
public class Cupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate fechaExpiracion;
    private String codigo;

    private double porcentajeDescuento; // 10.0 = 10%
}


