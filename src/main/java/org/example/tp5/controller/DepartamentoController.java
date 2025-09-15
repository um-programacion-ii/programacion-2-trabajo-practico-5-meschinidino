package org.example.tp5.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.tp5.model.Departamento;
import org.example.tp5.service.DepartamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar Departamentos.
 */
@RestController
@RequestMapping("/api/departamentos")
@Validated
@Tag(name = "Departamentos", description = "Operaciones sobre departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    /**
     * Listar todos los departamentos.
     * GET /api/departamentos
     */
    @GetMapping
    @Operation(summary = "Listar departamentos", description = "Obtiene la lista completa de departamentos")
    public List<Departamento> obtenerTodos() {
        return departamentoService.obtenerTodos();
    }

    /**
     * Obtener un departamento por ID.
     * GET /api/departamentos/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar departamento por ID", description = "Obtiene un departamento por su identificador")
    public Departamento obtenerPorId(@Parameter(description = "ID del departamento") @PathVariable Long id) {
        return departamentoService.buscarPorId(id);
    }

    /**
     * Crear un nuevo departamento.
     * POST /api/departamentos
     */
    @PostMapping
    @Operation(summary = "Crear departamento", description = "Crea un nuevo departamento")
    public ResponseEntity<Departamento> crear(@jakarta.validation.Valid @RequestBody Departamento departamento) {
        Departamento creado = departamentoService.guardar(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Actualizar un departamento existente.
     * PUT /api/departamentos/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar departamento", description = "Actualiza los datos de un departamento")
    public Departamento actualizar(@Parameter(description = "ID del departamento") @PathVariable Long id,
                                   @jakarta.validation.Valid @RequestBody Departamento departamento) {
        return departamentoService.actualizar(id, departamento);
    }

    /**
     * Eliminar un departamento por ID.
     * DELETE /api/departamentos/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar departamento", description = "Elimina un departamento por su ID")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del departamento") @PathVariable Long id) {
        departamentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
