package org.example.tp5.service;

import org.example.tp5.model.Proyecto;

import java.util.List;

public interface ProyectoService {
    Proyecto guardar(Proyecto proyecto);
    Proyecto buscarPorId(Long id);
    List<Proyecto> obtenerTodos();
    Proyecto actualizar(Long id, Proyecto proyecto);
    void eliminar(Long id);

    // Consulta personalizada de Etapa 2
    List<Proyecto> buscarActivos();
}
