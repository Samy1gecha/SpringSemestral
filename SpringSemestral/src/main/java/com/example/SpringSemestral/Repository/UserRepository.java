package com.example.SpringSemestral.Repository;

import com.example.SpringSemestral.Model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository; //con el JPA no necesitas implementar métodos básicos como guardar, eliminar, buscar por id, etc.
import org.springframework.data.jpa.repository.Query;              // Import de @Query
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}

