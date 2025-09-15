package org.example.tp5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tp5.exception.GlobalExceptionHandler;
import org.example.tp5.model.Departamento;
import org.example.tp5.model.Empleado;
import org.example.tp5.service.EmpleadoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmpleadoController.class)
@Import(GlobalExceptionHandler.class)
class EmpleadoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EmpleadoService empleadoService;

    private Empleado sampleEmpleado() {
        Departamento dep = new Departamento();
        dep.setId(1L);
        dep.setNombre("IT");
        Empleado e = new Empleado();
        e.setId(10L);
        e.setNombre("Ana");
        e.setApellido("Perez");
        e.setEmail("ana@example.com");
        e.setFechaContratacion(LocalDate.of(2022,1,10));
        e.setSalario(new BigDecimal("100000"));
        e.setDepartamento(dep);
        return e;
    }

    @Test
    @DisplayName("GET /api/empleados devuelve 200 y lista JSON")
    void getAll() throws Exception {
        given(empleadoService.obtenerTodos()).willReturn(List.of(sampleEmpleado()));
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Ana")));
    }

    @Test
    @DisplayName("GET /api/empleados/{id} cuando servicio lanza IllegalArgumentException -> 404 con ApiError")
    void getByIdNotFound() throws Exception {
        given(empleadoService.buscarPorId(99L)).willThrow(new IllegalArgumentException("Empleado no encontrado: 99"));
        mockMvc.perform(get("/api/empleados/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("99")));
    }

    @Test
    @DisplayName("POST /api/empleados valido -> 201")
    void postValid() throws Exception {
        Empleado input = sampleEmpleado();
        input.setId(null);
        Empleado saved = sampleEmpleado();
        given(empleadoService.guardar(org.mockito.ArgumentMatchers.any(Empleado.class))).willReturn(saved);

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)));
    }

    @Test
    @DisplayName("POST /api/empleados invalido (faltan campos) -> 400")
    void postInvalid() throws Exception {
        // Falta nombre y departamento -> viola @NotBlank y @NotNull
        Empleado invalido = new Empleado();
        invalido.setApellido("Perez");
        invalido.setEmail("ana@example.com");
        invalido.setFechaContratacion(LocalDate.now());
        invalido.setSalario(new BigDecimal("1"));

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/empleados/{id} not found -> 404")
    void deleteNotFound() throws Exception {
        org.mockito.Mockito.doThrow(new IllegalArgumentException("Empleado no encontrado: 77"))
                .when(empleadoService).eliminar(77L);
        mockMvc.perform(delete("/api/empleados/77"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    @DisplayName("GET /api/empleados/salario sin max -> 400 por parámetro faltante")
    void salarioMissingParam() throws Exception {
        mockMvc.perform(get("/api/empleados/salario").param("min", "1000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/empleados/email/{email} devuelve 404 cuando Optional.empty()")
    void getByEmailNotFound() throws Exception {
        given(empleadoService.buscarPorEmail("x@example.com")).willReturn(Optional.empty());
        mockMvc.perform(get("/api/empleados/email/x@example.com"))
                .andExpect(status().isNotFound());
    }
}
