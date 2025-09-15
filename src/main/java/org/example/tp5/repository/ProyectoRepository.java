package org.example.tp5.repository;

import org.example.tp5.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    // Proyectos activos: fecha fin > hoy (CURRENT_DATE en JPQL)
    @Query("select p from Proyecto p where p.fechaFin > CURRENT_DATE")
    List<Proyecto> findActivos();
}
