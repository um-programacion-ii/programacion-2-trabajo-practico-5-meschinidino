package org.example.tp5.service;

import org.example.tp5.model.Departamento;
import org.example.tp5.model.Empleado;
import org.example.tp5.repository.EmpleadoRepository;
import org.example.tp5.service.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceImplTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;
    private Departamento departamento;

    @BeforeEach
    void setUp() {
        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("IT");

        empleado = new Empleado();
        empleado.setId(10L);
        empleado.setNombre("Ana");
        empleado.setApellido("Perez");
        empleado.setEmail("ana@example.com");
        empleado.setFechaContratacion(LocalDate.of(2022,1,10));
        empleado.setSalario(new BigDecimal("100000"));
        empleado.setDepartamento(departamento);
    }

    @Test
    @DisplayName("guardar() persiste y retorna el empleado")
    void guardarEmpleado() {
        given(empleadoRepository.save(any(Empleado.class))).willReturn(empleado);
        Empleado saved = empleadoService.guardar(new Empleado());
        assertEquals(empleado.getId(), saved.getId());
        verify(empleadoRepository).save(any(Empleado.class));
    }

    @Test
    @DisplayName("buscarPorId() retorna empleado si existe")
    void buscarPorIdOk() {
        given(empleadoRepository.findById(10L)).willReturn(Optional.of(empleado));
        Empleado found = empleadoService.buscarPorId(10L);
        assertEquals(10L, found.getId());
    }

    @Test
    @DisplayName("buscarPorId() lanza IllegalArgumentException si no existe")
    void buscarPorIdNotFound() {
        given(empleadoRepository.findById(99L)).willReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> empleadoService.buscarPorId(99L));
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("actualizar() lanza IllegalArgumentException si no existe")
    void actualizarNotFound() {
        given(empleadoRepository.existsById(123L)).willReturn(false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> empleadoService.actualizar(123L, empleado));
        assertTrue(ex.getMessage().contains("123"));
    }

    @Test
    @DisplayName("actualizar() persiste cuando existe")
    void actualizarOk() {
        given(empleadoRepository.existsById(10L)).willReturn(true);
        given(empleadoRepository.save(any(Empleado.class))).willAnswer(inv -> inv.getArgument(0));
        Empleado updated = empleadoService.actualizar(10L, empleado);
        assertEquals(10L, updated.getId());
        verify(empleadoRepository).save(empleado);
    }

    @Test
    @DisplayName("eliminar() lanza IllegalArgumentException si no existe")
    void eliminarNotFound() {
        given(empleadoRepository.existsById(55L)).willReturn(false);
        assertThrows(IllegalArgumentException.class, () -> empleadoService.eliminar(55L));
    }

    @Test
    @DisplayName("eliminar() llama a deleteById cuando existe")
    void eliminarOk() {
        given(empleadoRepository.existsById(10L)).willReturn(true);
        empleadoService.eliminar(10L);
        verify(empleadoRepository).deleteById(10L);
    }

    @Test
    @DisplayName("buscarPorDepartamentoNombre delega en repository")
    void buscarPorDepartamentoNombre() {
        given(empleadoRepository.findByDepartamento_Nombre("IT")).willReturn(List.of(empleado));
        List<Empleado> list = empleadoService.buscarPorDepartamentoNombre("IT");
        assertEquals(1, list.size());
        verify(empleadoRepository).findByDepartamento_Nombre("IT");
    }

    @Test
    @DisplayName("buscarPorRangoSalario delega en repository")
    void buscarPorRangoSalario() {
        BigDecimal min = new BigDecimal("90000");
        BigDecimal max = new BigDecimal("120000");
        given(empleadoRepository.findBySalarioBetween(min, max)).willReturn(List.of(empleado));
        List<Empleado> list = empleadoService.buscarPorRangoSalario(min, max);
        assertEquals(1, list.size());
        verify(empleadoRepository).findBySalarioBetween(min, max);
    }

    @Test
    @DisplayName("obtenerTodos() devuelve lista del repository")
    void obtenerTodos() {
        given(empleadoRepository.findAll()).willReturn(List.of(empleado));
        assertEquals(1, empleadoService.obtenerTodos().size());
        verify(empleadoRepository).findAll();
    }
}