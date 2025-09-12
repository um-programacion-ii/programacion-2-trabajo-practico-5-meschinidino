package org.example.tp5.repository;

import org.example.tp5.model.Departamento;
import org.example.tp5.model.Empleado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmpleadoRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Test
    @DisplayName("Buscar empleados por departamento y por rango de salario, y por email")
    void findByDepartamentoAndSalarioBetweenAndEmail() {
        // given: dos departamentos y tres empleados
        Departamento it = new Departamento();
        it.setNombre("IT");
        it.setDescripcion("Tecnologia");
        it = em.persistFlushFind(it);

        Departamento hr = new Departamento();
        hr.setNombre("HR");
        hr.setDescripcion("Recursos Humanos");
        hr = em.persistFlushFind(hr);

        Empleado e1 = new Empleado();
        e1.setNombre("Ana");
        e1.setApellido("Perez");
        e1.setEmail("ana@example.com");
        e1.setFechaContratacion(LocalDate.of(2022, 1, 10));
        e1.setSalario(new BigDecimal("100000"));
        e1.setDepartamento(it);
        e1 = em.persistFlushFind(e1);

        Empleado e2 = new Empleado();
        e2.setNombre("Bruno");
        e2.setApellido("Diaz");
        e2.setEmail("bruno@example.com");
        e2.setFechaContratacion(LocalDate.of(2023, 3, 5));
        e2.setSalario(new BigDecimal("150000"));
        e2.setDepartamento(it);
        e2 = em.persistFlushFind(e2);

        Empleado e3 = new Empleado();
        e3.setNombre("Carla");
        e3.setApellido("Lopez");
        e3.setEmail("carla@example.com");
        e3.setFechaContratacion(LocalDate.of(2021, 7, 20));
        e3.setSalario(new BigDecimal("90000"));
        e3.setDepartamento(hr);
        e3 = em.persistFlushFind(e3);

        em.clear();

        final Long e1Id = e1.getId();
        final Long e2Id = e2.getId();

        // when: buscar por departamento IT
        List<Empleado> itEmployees = empleadoRepository.findByDepartamento_Id(it.getId());
        // and: buscar por rango de salario [95k, 120k]
        List<Empleado> salaryRange = empleadoRepository.findBySalarioBetween(new BigDecimal("95000"), new BigDecimal("120000"));
        // and: buscar por email exacto
        Optional<Empleado> byEmail = empleadoRepository.findByEmail("bruno@example.com");

        // then
        assertEquals(2, itEmployees.size(), "IT debe tener 2 empleados");
        assertTrue(itEmployees.stream().anyMatch(emp -> emp.getId().equals(e1Id)));
        assertTrue(itEmployees.stream().anyMatch(emp -> emp.getId().equals(e2Id)));

        assertEquals(1, salaryRange.size(), "El rango debe incluir solo 1 empleado");
        assertEquals(e1Id, salaryRange.get(0).getId());

        assertTrue(byEmail.isPresent(), "Debe encontrar empleado por email");
        assertEquals(e2Id, byEmail.get().getId());
    }
}
