package org.example.tp5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tp5.model.Departamento;
import org.example.tp5.model.Empleado;
import org.example.tp5.repository.DepartamentoRepository;
import org.example.tp5.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmpleadoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento dep;

    @BeforeEach
    void setupData() {
        empleadoRepository.deleteAll();
        departamentoRepository.deleteAll();

        dep = new Departamento();
        dep.setNombre("IT");
        dep.setDescripcion("Tecnologia");
        dep = departamentoRepository.save(dep);

        Empleado e = new Empleado();
        e.setNombre("Ana");
        e.setApellido("Perez");
        e.setEmail("ana-int@example.com");
        e.setFechaContratacion(LocalDate.of(2022, 1, 10));
        e.setSalario(new BigDecimal("100000"));
        e.setDepartamento(dep);
        empleadoRepository.save(e);
    }

    @Test
    @DisplayName("IT Controller: GET /api/empleados devuelve lista con 1 elemento")
    void getAll() throws Exception {
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("ana-int@example.com")));
    }

    @Test
    @DisplayName("IT Controller: POST /api/empleados valido -> 201 y persistido")
    void postValid() throws Exception {
        Empleado nuevo = new Empleado();
        nuevo.setNombre("Bruno");
        nuevo.setApellido("Diaz");
        nuevo.setEmail("bruno-int@example.com");
        nuevo.setFechaContratacion(LocalDate.of(2023, 3, 5));
        nuevo.setSalario(new BigDecimal("150000"));
        nuevo.setDepartamento(dep);

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email", is("bruno-int@example.com")));
    }

    @Test
    @DisplayName("IT Controller: DELETE /api/empleados/{id} -> 204")
    void deleteOk() throws Exception {
        Long id = empleadoRepository.findAll().get(0).getId();
        mockMvc.perform(delete("/api/empleados/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("IT Controller: POST inválido -> 400")
    void postInvalid() throws Exception {
        Empleado invalido = new Empleado();
        invalido.setApellido("Perez");
        invalido.setEmail("bad@example.com");
        invalido.setFechaContratacion(LocalDate.now());
        invalido.setSalario(new BigDecimal("1"));
        // Falta nombre y departamento
        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }
}
