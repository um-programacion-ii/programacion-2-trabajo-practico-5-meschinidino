package org.example.tp5.service;

import org.example.tp5.model.Departamento;

import java.util.List;

public interface DepartamentoService {
    Departamento guardar(Departamento departamento);
    Departamento buscarPorId(Long id);
    List<Departamento> obtenerTodos();
    Departamento actualizar(Long id, Departamento departamento);
    void eliminar(Long id);
}
