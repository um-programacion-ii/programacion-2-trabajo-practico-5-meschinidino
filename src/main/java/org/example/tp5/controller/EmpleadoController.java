package org.example.tp5.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.tp5.model.Empleado;
import org.example.tp5.service.EmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para gestionar Empleados.
 * Endpoints básicos de CRUD y consultas personalizadas por departamento, salario y email.
 */
@RestController
@RequestMapping("/api/empleados")
@Validated
@Tag(name = "Empleados", description = "Operaciones sobre empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    /**
     * Devuelve todos los empleados.
     * GET /api/empleados
     */
    @GetMapping
    @Operation(summary = "Listar empleados", description = "Obtiene la lista completa de empleados")
    public List<Empleado> obtenerTodos() {
        return empleadoService.obtenerTodos();
    }

    /**
     * Devuelve un empleado por ID.
     * GET /api/empleados/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Obtiene un empleado por su identificador")
    public Empleado obtenerPorId(@Parameter(description = "ID del empleado") @PathVariable Long id) {
        return empleadoService.buscarPorId(id);
    }

    /**
     * Crea un nuevo empleado.
     * POST /api/empleados
     */
    @PostMapping
    @Operation(summary = "Crear empleado", description = "Crea un nuevo empleado y retorna el recurso creado")
    public ResponseEntity<Empleado> crear(@jakarta.validation.Valid @RequestBody Empleado empleado) {
        Empleado creado = empleadoService.guardar(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * Actualiza un empleado existente.
     * PUT /api/empleados/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empleado", description = "Actualiza un empleado existente por ID")
    public Empleado actualizar(@Parameter(description = "ID del empleado") @PathVariable Long id,
                               @jakarta.validation.Valid @RequestBody Empleado empleado) {
        return empleadoService.actualizar(id, empleado);
    }

    /**
     * Elimina un empleado por ID.
     * DELETE /api/empleados/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado por su ID")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del empleado") @PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Devuelve empleados por nombre de departamento.
     * GET /api/empleados/departamento/{nombre}
     */
    @GetMapping("/departamento/{nombre}")
    @Operation(summary = "Buscar por departamento", description = "Lista empleados filtrando por nombre de departamento")
    public List<Empleado> obtenerPorDepartamento(@Parameter(description = "Nombre del departamento") @PathVariable String nombre) {
        return empleadoService.buscarPorDepartamentoNombre(nombre);
    }

    /**
     * Devuelve empleados dentro de un rango salarial.
     * GET /api/empleados/salario?min=X&max=Y
     */
    @GetMapping("/salario")
    @Operation(summary = "Buscar por rango salarial", description = "Lista empleados con salario entre min y max, inclusivo")
    public List<Empleado> obtenerPorRangoSalario(@Parameter(description = "Salario mínimo") @RequestParam BigDecimal min,
                                                 @Parameter(description = "Salario máximo") @RequestParam BigDecimal max) {
        return empleadoService.buscarPorRangoSalario(min, max);
    }

    /**
     * Busca un empleado por email.
     * GET /api/empleados/email/{email}
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar por email", description = "Obtiene un empleado por su email exacto")
    public ResponseEntity<Empleado> obtenerPorEmail(@Parameter(description = "Email del empleado") @PathVariable String email) {
        return empleadoService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
