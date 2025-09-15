package org.example.tp5.repository;

import org.example.tp5.model.Departamento;
import org.example.tp5.model.Empleado;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DataJpaTest
@ActiveProfiles("postgres")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmpleadoRepositoryPostgresIT {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private static boolean portOpen(String host, int port, int timeoutMs) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @BeforeAll
    static void checkPostgresAvailable() {
        assumeTrue(portOpen("localhost", 5432, 500), () -> "[Postgres IT] Saltando tests: puerto 5432 no disponible. ¿Está levantado docker compose?");
    }

    @BeforeEach
    void cleanDb() {
        empleadoRepository.deleteAll();
        departamentoRepository.deleteAll();
    }

    @Test
    @DisplayName("Postgres: CRUD/consultas básicas de EmpleadoRepository")
    void postgresCrudAndQueries() {
        Departamento it = new Departamento();
        it.setNombre("IT");
        it.setDescripcion("Tecnologia");
        it = departamentoRepository.save(it);

        Departamento hr = new Departamento();
        hr.setNombre("HR");
        hr.setDescripcion("Recursos Humanos");
        hr = departamentoRepository.save(hr);

        Empleado e1 = new Empleado();
        e1.setNombre("Ana");
        e1.setApellido("Perez");
        e1.setEmail("ana-pg@example.com");
        e1.setFechaContratacion(LocalDate.of(2022, 1, 10));
        e1.setSalario(new BigDecimal("100000"));
        e1.setDepartamento(it);
        e1 = empleadoRepository.save(e1);

        Empleado e2 = new Empleado();
        e2.setNombre("Bruno");
        e2.setApellido("Diaz");
        e2.setEmail("bruno-pg@example.com");
        e2.setFechaContratacion(LocalDate.of(2023, 3, 5));
        e2.setSalario(new BigDecimal("150000"));
        e2.setDepartamento(it);
        e2 = empleadoRepository.save(e2);

        Empleado e3 = new Empleado();
        e3.setNombre("Carla");
        e3.setApellido("Lopez");
        e3.setEmail("carla-pg@example.com");
        e3.setFechaContratacion(LocalDate.of(2021, 7, 20));
        e3.setSalario(new BigDecimal("90000"));
        e3.setDepartamento(hr);
        e3 = empleadoRepository.save(e3);

        // when
        List<Empleado> itEmployees = empleadoRepository.findByDepartamento_Nombre("IT");
        List<Empleado> salaryRange = empleadoRepository.findBySalarioBetween(new BigDecimal("95000"), new BigDecimal("120000"));
        Optional<Empleado> byEmail = empleadoRepository.findByEmail("bruno-pg@example.com");

        // then
        assertEquals(2, itEmployees.size(), "IT debe tener 2 empleados (Postgres)");
        assertEquals(1, salaryRange.size(), "El rango debe incluir solo 1 empleado (Postgres)");
        assertTrue(byEmail.isPresent(), "Debe encontrar empleado por email (Postgres)");
        assertEquals(e2.getId(), byEmail.get().getId());
    }
}
