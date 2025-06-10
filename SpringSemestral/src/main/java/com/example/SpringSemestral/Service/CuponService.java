package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Controlador.CuponController;
import com.example.SpringSemestral.Model.Cupon;
import com.example.SpringSemestral.Repository.CuponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuponService {

    @Autowired
    private CuponRepository cuponRepository;
    @Autowired
    private CuponController cuponController;

    public List<Cupon> listarCupones() {
        return cuponRepository.findAll();
    }

    public Cupon obtenerPorId(int id) {
        return cuponRepository.findById(id).orElse(null);
    }

    public Cupon crearCupon(Cupon cupon) {
        return cuponRepository.save(cupon);
    }

    public Cupon actualizarCupon(int id, Cupon cuponActualizado) {
        Optional<Cupon> cuponExistente = cuponRepository.findById(id);
        if (cuponExistente.isPresent()) {
            Cupon cupon = cuponExistente.get();
            cupon.setCodigo(cuponActualizado.getCodigo());
            cupon.setPorcentajeDescuento(cuponActualizado.getPorcentajeDescuento());
            cupon.setFechaExpiracion(cuponActualizado.getFechaExpiracion());
            return cuponRepository.save(cupon);

        }
        return null;
    }

    public boolean eliminarCupon(int id) {
        if (cuponRepository.existsById(id)) {
            cuponRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Cupon buscarPorCodigo(String codigo) {
        return cuponRepository.findByCodigo(codigo).orElse(null);
    }
}

