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
class EmpleadoDepartamentoJpaTest {

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Un empleado pertenece a un departamento y el departamento lista a sus empleados")
    void empleadoManyToOneDepartamentoAndDepartamentoOneToManyEmpleados() {
        // given
        Departamento dep = new Departamento();
        dep.setNombre("IT");
        dep.setDescripcion("Tecnologia");
        dep = em.persistFlushFind(dep);

        Empleado emp = new Empleado();
        emp.setNombre("Ana");
        emp.setApellido("Perez");
        emp.setEmail("ana.perez@example.com");
        emp.setFechaContratacion(LocalDate.of(2020, 1, 15));
        emp.setSalario(new BigDecimal("123456.78"));
        emp.setDepartamento(dep);
        emp = em.persistFlushFind(emp);

        em.clear();

        // when
        Empleado foundEmp = em.find(Empleado.class, emp.getId());
        Departamento foundDep = em.find(Departamento.class, dep.getId());

        // then
        assertNotNull(foundEmp.getDepartamento(), "El empleado debe tener departamento");
        assertEquals(dep.getId(), foundEmp.getDepartamento().getId(), "El departamento del empleado coincide");

        assertNotNull(foundDep.getEmpleados(), "La lista de empleados no debe ser null");
        assertEquals(1, foundDep.getEmpleados().size(), "El departamento debe tener 1 empleado");
        assertEquals(foundEmp.getId(), foundDep.getEmpleados().get(0).getId(), "El empleado asociado debe coincidir");
    }
}
