package org.example.tp5.repository;

import org.example.tp5.model.Departamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DepartamentoRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Test
    @DisplayName("CRUD básico con DepartamentoRepository: save, findById, findAll, delete")
    void basicCrud() {
        // given: dos departamentos persistidos
        Departamento it = new Departamento();
        it.setNombre("IT");
        it.setDescripcion("Tecnologia");
        it = em.persistFlushFind(it);

        Departamento hr = new Departamento();
        hr.setNombre("HR");
        hr.setDescripcion("Recursos Humanos");
        hr = em.persistFlushFind(hr);

        em.clear();

        // when: findAll
        final Long itId = it.getId();
        final Long hrId = hr.getId();
        List<Departamento> all = departamentoRepository.findAll();

        // then
        assertEquals(2, all.size(), "Debe haber 2 departamentos");
        assertTrue(all.stream().anyMatch(d -> d.getId().equals(itId)));
        assertTrue(all.stream().anyMatch(d -> d.getId().equals(hrId)));

        // when: findById
        Optional<Departamento> foundIt = departamentoRepository.findById(itId);
        assertTrue(foundIt.isPresent(), "Debe encontrar departamento IT por ID");
        assertEquals("IT", foundIt.get().getNombre());

        // when: delete
        departamentoRepository.deleteById(hrId);
        departamentoRepository.flush();

        List<Departamento> afterDelete = departamentoRepository.findAll();
        assertEquals(1, afterDelete.size(), "Debe quedar 1 departamento tras borrar HR");
        assertEquals(itId, afterDelete.get(0).getId());
    }
}
