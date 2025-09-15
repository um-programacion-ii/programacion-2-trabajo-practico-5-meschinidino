package org.example.tp5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tp5.exception.GlobalExceptionHandler;
import org.example.tp5.model.Proyecto;
import org.example.tp5.service.ProyectoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProyectoController.class)
@Import(GlobalExceptionHandler.class)
class ProyectoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProyectoService proyectoService;

    private Proyecto sample() {
        Proyecto p = new Proyecto();
        p.setId(1L);
        p.setNombre("Proyecto A");
        p.setFechaInicio(LocalDate.now());
        p.setFechaFin(LocalDate.now().plusDays(1));
        return p;
    }

    @Test
    @DisplayName("GET /api/proyectos/activos -> 200 y lista")
    void activos() throws Exception {
        given(proyectoService.buscarActivos()).willReturn(List.of(sample()));
        mockMvc.perform(get("/api/proyectos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Proyecto A")));
    }

    @Test
    @DisplayName("POST /api/proyectos invalido (sin nombre) -> 400")
    void postInvalid() throws Exception {
        Proyecto invalido = new Proyecto();
        mockMvc.perform(post("/api/proyectos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/proyectos/{id} not found -> 404")
    void getNotFound() throws Exception {
        org.mockito.BDDMockito.willThrow(new IllegalArgumentException("Proyecto no encontrado: 77"))
                .given(proyectoService).buscarPorId(77L);
        mockMvc.perform(get("/api/proyectos/77"))
                .andExpect(status().isNotFound());
    }
}

