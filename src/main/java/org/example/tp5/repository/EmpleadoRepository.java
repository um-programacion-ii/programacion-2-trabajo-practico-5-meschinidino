package org.example.tp5.repository;

import org.example.tp5.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    // Buscar empleados por departamento (por id de departamento)
    List<Empleado> findByDepartamento_Id(Long departamentoId);

    // Buscar empleados por departamento (por nombre de departamento)
    List<Empleado> findByDepartamento_Nombre(String nombre);

    // Buscar empleados por rango de salario
    List<Empleado> findBySalarioBetween(BigDecimal min, BigDecimal max);

    // Buscar empleado por email
    Optional<Empleado> findByEmail(String email);
}
