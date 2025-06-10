package com.example.SpringSemestral.Controlador;

import com.example.SpringSemestral.Model.Cupon;
import com.example.SpringSemestral.Repository.CuponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cupones")
public class CuponController {

    @Autowired
    private CuponRepository cuponRepository;

    @PostMapping
    public String crearCupon(@RequestBody Cupon cupon) {
        if (cupon.getCodigo() == null || cupon.getCodigo().isBlank()) {
            return "Código del cupón es obligatorio";
        }

        if (cupon.getPorcentajeDescuento() <= 0 || cupon.getPorcentajeDescuento() > 100) {
            return "El descuento debe ser mayor a 0 y como máximo 100%";
        }

        if (cuponRepository.findByCodigo(cupon.getCodigo()).isPresent()) {
            return "Ya existe un cupón con ese código";
        }

        cuponRepository.save(cupon);
        return "Cupón creado con éxito";
    }
    @DeleteMapping("/codigo/{codigo}")
    public String eliminarCuponPorCodigo(@PathVariable String codigo) {
        Cupon cupon = cuponRepository.findByCodigo(codigo).orElse(null);
        if (cupon == null) {
            return "Cupón no encontrado";
        }
        cuponRepository.delete(cupon);
        return "Cupón eliminado correctamente";
    }


    @GetMapping
    public List<Cupon> listarCupones() {
        return cuponRepository.findAll();
    }

    @GetMapping("/{codigo}")
    public Cupon buscarPorCodigo(@PathVariable String codigo) {
        return cuponRepository.findByCodigo(codigo).orElse(null);
    }
    @PostMapping("/actualizar")
    public String actualizarCupon(
            @RequestParam(required = false) Integer id,
            @RequestBody Cupon cuponDatos) {

        if (id == null) {
            return "El id es obligatorio para actualizar un cupón";
        }

        return cuponRepository.findById(id)
                .map(cuponExistente -> {
                    // Actualizas los campos que quieras con los datos entrantes
                    if (cuponDatos.getCodigo() != null && !cuponDatos.getCodigo().isBlank()) {
                        cuponExistente.setCodigo(cuponDatos.getCodigo());
                    }
                    if (cuponDatos.getPorcentajeDescuento() > 0 && cuponDatos.getPorcentajeDescuento() <= 100) {
                        cuponExistente.setPorcentajeDescuento(cuponDatos.getPorcentajeDescuento());
                    }
                    cuponRepository.save(cuponExistente);
                    return "Cupón actualizado correctamente";
                })
                .orElse("Cupón con id " + id + " no encontrado");
    }

}

