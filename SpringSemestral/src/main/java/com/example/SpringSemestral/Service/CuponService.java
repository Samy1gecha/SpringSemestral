package com.example.SpringSemestral.Service;

import com.example.SpringSemestral.Model.Cupon;
import com.example.SpringSemestral.Repository.CuponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CuponService {

    @Autowired
    private CuponRepository repo;

    public Cupon create(Cupon c) {
        return repo.save(c);
    }

    @Transactional(readOnly = true)
    public Optional<Cupon> getById(int id) {
        return repo.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Cupon> getAll() {
        return repo.findAll();
    }

    @Transactional
    public boolean deleteById(int id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}

