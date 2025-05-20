package com.example.SpringSemestral.Repository;


import com.example.SpringSemestral.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByNombre(String nombre);
}
