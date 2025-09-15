package org.example.tp5.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.tp5.model.Proyecto;
import org.example.tp5.service.ProyectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar Proyectos.
 */
@RestController
@RequestMapping("/api/proyectos")
@Validated
@Tag(name = "Proyectos", description = "Operaciones sobre proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    /**
     * Listar todos los proyectos.
     * GET /api/proyectos
     */
    @GetMapping
    @Operation(summary = "Listar proyectos", description = "Obtiene la lista completa de proyectos")
    public List<Proyecto> obtenerTodos() {
        return proyectoService.obtenerTodos();
    }

    /**
     * Obtener proyecto por ID.
     * GET /api/proyectos/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar proyecto por ID", description = "Obtiene un proyecto por su identificador")
    public Proyecto obtenerPorId(@Parameter(description = "ID del proyecto") @PathVariable Long id) {
        return proyectoService.buscarPorId(id);
    }

    /**
     * Crear proyecto.
     * POST /api/proyectos
     */
    @PostMapping
    @Operation(summary = "Crear proyecto", description = "Crea un nuevo proyecto")
    public ResponseEntity<Proyecto> crear(@jakarta.validation.Valid @RequestBody Proyecto proyecto) {
        Proyecto creado = proyectoService.guardar(proyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Actualizar proyecto.
     * PUT /api/proyectos/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proyecto", description = "Actualiza los datos de un proyecto")
    public Proyecto actualizar(@Parameter(description = "ID del proyecto") @PathVariable Long id,
                               @jakarta.validation.Valid @RequestBody Proyecto proyecto) {
        return proyectoService.actualizar(id, proyecto);
    }

    /**
     * Eliminar proyecto por ID.
     * DELETE /api/proyectos/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proyecto", description = "Elimina un proyecto por su ID")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del proyecto") @PathVariable Long id) {
        proyectoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Listar proyectos activos (fecha fin > hoy).
     * GET /api/proyectos/activos
     */
    @GetMapping("/activos")
    @Operation(summary = "Proyectos activos", description = "Devuelve proyectos con fecha de fin posterior a hoy")
    public List<Proyecto> activos() {
        return proyectoService.buscarActivos();
    }
}
