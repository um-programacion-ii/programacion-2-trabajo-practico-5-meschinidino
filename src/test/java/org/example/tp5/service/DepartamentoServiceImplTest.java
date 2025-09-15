package org.example.tp5.service;

import org.example.tp5.model.Departamento;
import org.example.tp5.repository.DepartamentoRepository;
import org.example.tp5.service.impl.DepartamentoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartamentoServiceImplTest {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private DepartamentoServiceImpl departamentoService;

    @Test
    @DisplayName("buscarPorId retorna y not-found lanza IllegalArgumentException")
    void buscarPorId() {
        Departamento it = new Departamento();
        it.setId(1L);
        it.setNombre("IT");

        given(departamentoRepository.findById(1L)).willReturn(Optional.of(it));
        assertEquals(1L, departamentoService.buscarPorId(1L).getId());

        given(departamentoRepository.findById(99L)).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> departamentoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("guardar/actualizar/eliminar: flujos principales")
    void crudFlujos() {
        Departamento d = new Departamento();
        d.setNombre("HR");
        given(departamentoRepository.save(any(Departamento.class))).willAnswer(inv -> {
            Departamento arg = inv.getArgument(0);
            if (arg.getId() == null) arg.setId(10L);
            return arg;
        });

        Departamento saved = departamentoService.guardar(d);
        assertEquals(10L, saved.getId());
        verify(departamentoRepository).save(d);

        given(departamentoRepository.existsById(10L)).willReturn(true);
        Departamento updated = departamentoService.actualizar(10L, saved);
        assertEquals(10L, updated.getId());

        given(departamentoRepository.existsById(11L)).willReturn(false);
        assertThrows(IllegalArgumentException.class, () -> departamentoService.actualizar(11L, d));

        given(departamentoRepository.existsById(10L)).willReturn(true);
        departamentoService.eliminar(10L);
        verify(departamentoRepository).deleteById(10L);

        given(departamentoRepository.existsById(12L)).willReturn(false);
        assertThrows(IllegalArgumentException.class, () -> departamentoService.eliminar(12L));
    }

    @Test
    @DisplayName("obtenerTodos delega en repository")
    void obtenerTodos() {
        given(departamentoRepository.findAll()).willReturn(List.of(new Departamento()));
        assertEquals(1, departamentoService.obtenerTodos().size());
        verify(departamentoRepository).findAll();
    }
}
