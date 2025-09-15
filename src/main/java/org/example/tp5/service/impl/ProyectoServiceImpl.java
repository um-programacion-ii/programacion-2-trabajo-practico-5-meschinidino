package org.example.tp5.service.impl;

import org.example.tp5.model.Proyecto;
import org.example.tp5.repository.ProyectoRepository;
import org.example.tp5.service.ProyectoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    @Transactional
    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto buscarPorId(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado: " + id));
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    @Transactional
    public Proyecto actualizar(Long id, Proyecto proyecto) {
        if (!proyectoRepository.existsById(id)) {
            throw new IllegalArgumentException("Proyecto no encontrado: " + id);
        }
        proyecto.setId(id);
        return proyectoRepository.save(proyecto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new IllegalArgumentException("Proyecto no encontrado: " + id);
        }
        proyectoRepository.deleteById(id);
    }

    @Override
    public List<Proyecto> buscarActivos() {
        return proyectoRepository.findActivos();
    }
}
