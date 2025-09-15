package org.example.tp5.service;

import org.example.tp5.model.Proyecto;
import org.example.tp5.repository.ProyectoRepository;
import org.example.tp5.service.impl.ProyectoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectoServiceImplTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private ProyectoServiceImpl proyectoService;

    @Test
    @DisplayName("guardar y buscarPorId / not found")
    void guardarYBuscar() {
        Proyecto p = new Proyecto();
        p.setId(5L);
        p.setNombre("Sistema A");
        p.setFechaInicio(LocalDate.now());
        given(proyectoRepository.save(any(Proyecto.class))).willReturn(p);
        Proyecto saved = proyectoService.guardar(new Proyecto());
        assertEquals(5L, saved.getId());

        given(proyectoRepository.findById(5L)).willReturn(Optional.of(p));
        assertEquals(5L, proyectoService.buscarPorId(5L).getId());

        given(proyectoRepository.findById(99L)).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> proyectoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("actualizar/eliminar: existen y no existen")
    void actualizarEliminar() {
        Proyecto p = new Proyecto();
        p.setNombre("P");
        given(proyectoRepository.existsById(1L)).willReturn(true);
        given(proyectoRepository.save(any(Proyecto.class))).willAnswer(inv -> {
            Proyecto arg = inv.getArgument(0);
            return arg;
        });
        Proyecto updated = proyectoService.actualizar(1L, p);
        assertEquals(1L, updated.getId());

        given(proyectoRepository.existsById(2L)).willReturn(false);
        assertThrows(IllegalArgumentException.class, () -> proyectoService.actualizar(2L, p));

        given(proyectoRepository.existsById(1L)).willReturn(true);
        proyectoService.eliminar(1L);
        verify(proyectoRepository).deleteById(1L);

        given(proyectoRepository.existsById(3L)).willReturn(false);
        assertThrows(IllegalArgumentException.class, () -> proyectoService.eliminar(3L));
    }

    @Test
    @DisplayName("buscarActivos delega en repository")
    void buscarActivos() {
        given(proyectoRepository.findActivos()).willReturn(List.of(new Proyecto()));
        assertEquals(1, proyectoService.buscarActivos().size());
        verify(proyectoRepository).findActivos();
    }

    @Test
    @DisplayName("obtenerTodos delega en repository")
    void obtenerTodos() {
        given(proyectoRepository.findAll()).willReturn(List.of(new Proyecto()));
        assertEquals(1, proyectoService.obtenerTodos().size());
        verify(proyectoRepository).findAll();
    }
}