package org.example.tp5.repository;

import org.example.tp5.model.Proyecto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProyectoRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Test
    @DisplayName("Buscar proyectos activos: fechaFin > hoy")
    void findActivos() {
        // given: 3 proyectos con distintas fechas fin
        LocalDate today = LocalDate.now();

        Proyecto pasado = new Proyecto();
        pasado.setNombre("Pasado");
        pasado.setDescripcion("Proyecto pasado");
        pasado.setFechaInicio(today.minusMonths(2));
        pasado.setFechaFin(today.minusDays(1));
        em.persist(pasado);

        Proyecto hoy = new Proyecto();
        hoy.setNombre("Hoy");
        hoy.setDescripcion("Termina hoy");
        hoy.setFechaInicio(today.minusMonths(1));
        hoy.setFechaFin(today); // No debe contarse como activo
        em.persist(hoy);

        Proyecto futuro = new Proyecto();
        futuro.setNombre("Futuro");
        futuro.setDescripcion("Proyecto activo");
        futuro.setFechaInicio(today);
        futuro.setFechaFin(today.plusDays(1));
        em.persist(futuro);

        em.flush();
        em.clear();

        // when
        List<Proyecto> activos = proyectoRepository.findActivos();

        // then
        assertNotNull(activos);
        assertEquals(1, activos.size(), "Debe haber 1 proyecto activo (fecha fin > hoy)");
        assertEquals("Futuro", activos.get(0).getNombre());
    }
}
