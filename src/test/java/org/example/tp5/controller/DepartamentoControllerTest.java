package org.example.tp5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tp5.exception.GlobalExceptionHandler;
import org.example.tp5.model.Departamento;
import org.example.tp5.service.DepartamentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DepartamentoController.class)
@Import(GlobalExceptionHandler.class)
class DepartamentoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DepartamentoService departamentoService;

    private Departamento sample() {
        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("IT");
        d.setDescripcion("Tecnologia");
        return d;
    }

    @Test
    @DisplayName("GET /api/departamentos devuelve 200")
    void getAll() throws Exception {
        given(departamentoService.obtenerTodos()).willReturn(List.of(sample()));
        mockMvc.perform(get("/api/departamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("IT")));
    }

    @Test
    @DisplayName("POST /api/departamentos invalido -> 400")
    void postInvalid() throws Exception {
        Departamento invalido = new Departamento(); // falta nombre
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/departamentos/{id} not found -> 404")
    void getNotFound() throws Exception {
        given(departamentoService.buscarPorId(77L)).willThrow(new IllegalArgumentException("Departamento no encontrado: 77"));
        mockMvc.perform(get("/api/departamentos/77"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    @DisplayName("DELETE /api/departamentos/{id} not found -> 404")
    void deleteNotFound() throws Exception {
        org.mockito.Mockito.doThrow(new IllegalArgumentException("Departamento no encontrado: 77"))
                .when(departamentoService).eliminar(77L);
        mockMvc.perform(delete("/api/departamentos/77"))
                .andExpect(status().isNotFound());
    }
}
