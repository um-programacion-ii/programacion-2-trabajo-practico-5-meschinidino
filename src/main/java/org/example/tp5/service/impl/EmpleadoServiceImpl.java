package org.example.tp5.service.impl;

import org.example.tp5.model.Empleado;
import org.example.tp5.repository.EmpleadoRepository;
import org.example.tp5.service.EmpleadoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    @Transactional
    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado buscarPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado: " + id));
    }

    @Override
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    @Transactional
    public Empleado actualizar(Long id, Empleado empleado) {
        if (!empleadoRepository.existsById(id)) {
            throw new IllegalArgumentException("Empleado no encontrado: " + id);
        }
        empleado.setId(id);
        return empleadoRepository.save(empleado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new IllegalArgumentException("Empleado no encontrado: " + id);
        }
        empleadoRepository.deleteById(id);
    }

    @Override
    public List<Empleado> buscarPorDepartamento(Long departamentoId) {
        return empleadoRepository.findByDepartamento_Id(departamentoId);
    }

    @Override
    public List<Empleado> buscarPorRangoSalario(BigDecimal min, BigDecimal max) {
        return empleadoRepository.findBySalarioBetween(min, max);
    }

    @Override
    public Optional<Empleado> buscarPorEmail(String email) {
        return empleadoRepository.findByEmail(email);
    }
}
