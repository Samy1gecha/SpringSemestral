package com.example.SpringSemestral.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // si quieres almacenar la fecha de generación:
    private LocalDate fechaGeneracion;

    // constructor vacío
    public Reporte() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}
