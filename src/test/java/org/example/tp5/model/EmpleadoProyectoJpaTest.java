package org.example.tp5.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmpleadoProyectoJpaTest {

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Un empleado puede estar en muchos proyectos y un proyecto puede tener muchos empleados")
    void manyToManyEmpleadoProyecto() {
        // given: un empleado y dos proyectos
        // crear departamento requerido por @NotNull en Empleado.departamento
        Departamento dep = new Departamento();
        dep.setNombre("IT");
        dep.setDescripcion("Tecnologia");
        dep = em.persistFlushFind(dep);

        Empleado emp = new Empleado();
        emp.setNombre("Juan");
        emp.setApellido("Gomez");
        emp.setEmail("juan.gomez@example.com");
        emp.setFechaContratacion(LocalDate.of(2021, 6, 1));
        emp.setSalario(new BigDecimal("98765.43"));
        emp.setDepartamento(dep);
        emp = em.persistFlushFind(emp);

        Proyecto p1 = new Proyecto();
        p1.setNombre("Sistema A");
        p1.setDescripcion("Proyecto A");
        p1.setFechaInicio(LocalDate.of(2024, 1, 1));
        p1.setFechaFin(LocalDate.of(2024, 12, 31));
        p1 = em.persistFlushFind(p1);

        Proyecto p2 = new Proyecto();
        p2.setNombre("Sistema B");
        p2.setDescripcion("Proyecto B");
        p2.setFechaInicio(LocalDate.of(2024, 2, 1));
        p2.setFechaFin(LocalDate.of(2024, 11, 30));
        p2 = em.persistFlushFind(p2);

        // Asociar en el lado propietario (Empleado)
        emp.getProyectos().add(p1);
        emp.getProyectos().add(p2);
        emp = em.persistFlushFind(emp);

        em.clear();

        // when
        Empleado foundEmp = em.find(Empleado.class, emp.getId());
        Proyecto foundP1 = em.find(Proyecto.class, p1.getId());
        Proyecto foundP2 = em.find(Proyecto.class, p2.getId());

        // then: del lado del empleado
        assertEquals(2, foundEmp.getProyectos().size(), "El empleado debe tener 2 proyectos");
        assertTrue(foundEmp.getProyectos().stream().anyMatch(p -> p.getId().equals(foundP1.getId())));
        assertTrue(foundEmp.getProyectos().stream().anyMatch(p -> p.getId().equals(foundP2.getId())));

        // y del lado del proyecto (lado inverso)
        assertEquals(1, foundP1.getEmpleados().size(), "Proyecto 1 debe tener 1 empleado");
        assertEquals(foundEmp.getId(), foundP1.getEmpleados().iterator().next().getId());

        assertEquals(1, foundP2.getEmpleados().size(), "Proyecto 2 debe tener 1 empleado");
        assertEquals(foundEmp.getId(), foundP2.getEmpleados().iterator().next().getId());
    }
}
