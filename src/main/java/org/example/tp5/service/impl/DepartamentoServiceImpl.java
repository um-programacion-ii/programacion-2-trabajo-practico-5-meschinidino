package org.example.tp5.service.impl;

import org.example.tp5.model.Departamento;
import org.example.tp5.repository.DepartamentoRepository;
import org.example.tp5.service.DepartamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    @Transactional
    public Departamento guardar(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado: " + id));
    }

    @Override
    public List<Departamento> obtenerTodos() {
        return departamentoRepository.findAll();
    }

    @Override
    @Transactional
    public Departamento actualizar(Long id, Departamento departamento) {
        if (!departamentoRepository.existsById(id)) {
            throw new IllegalArgumentException("Departamento no encontrado: " + id);
        }
        departamento.setId(id);
        return departamentoRepository.save(departamento);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new IllegalArgumentException("Departamento no encontrado: " + id);
        }
        departamentoRepository.deleteById(id);
    }
}
