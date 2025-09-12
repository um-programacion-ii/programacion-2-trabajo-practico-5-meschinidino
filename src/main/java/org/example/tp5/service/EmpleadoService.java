package org.example.tp5.service;

import org.example.tp5.model.Empleado;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    Empleado guardar(Empleado empleado);
    Empleado buscarPorId(Long id);
    List<Empleado> obtenerTodos();
    Empleado actualizar(Long id, Empleado empleado);
    void eliminar(Long id);

    // Consultas personalizadas requeridas en Etapa 2
    List<Empleado> buscarPorDepartamento(Long departamentoId);
    List<Empleado> buscarPorRangoSalario(BigDecimal min, BigDecimal max);
    Optional<Empleado> buscarPorEmail(String email);
}
