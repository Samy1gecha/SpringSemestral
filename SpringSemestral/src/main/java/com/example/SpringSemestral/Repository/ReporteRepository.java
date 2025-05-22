package com.example.SpringSemestral.Repository;
import java.time.LocalDate;
import org.springframework.data.repository.query.Param;

import com.example.SpringSemestral.Model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<DetallePedido, Integer> {

    @Query("SELECT SUM(d.cantidad * d.precioUnitario) FROM DetallePedido d")
    Double obtenerTotalVentas();

    @Query("SELECT d.producto.nombre, SUM(d.cantidad) FROM DetallePedido d GROUP BY d.producto.nombre ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> obtenerProductosMasVendidos();

    @Query("SELECT d.pedido.cliente.username, SUM(d.cantidad * d.precioUnitario) FROM DetallePedido d GROUP BY d.pedido.cliente.username")
    List<Object[]> obtenerTotalPorCliente();
    @Query("""
    SELECT SUM(d.cantidad * d.precioUnitario)
    FROM DetallePedido d
    WHERE d.pedido.fecha BETWEEN :desde AND :hasta
""")
    Double obtenerTotalVentasPorFecha(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

}
