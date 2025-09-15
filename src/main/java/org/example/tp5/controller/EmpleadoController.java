package org.example.tp5.controller;

import org.example.tp5.model.Empleado;
import org.example.tp5.service.EmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@Validated
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public List<Empleado> obtenerTodos() {
        return empleadoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Empleado obtenerPorId(@PathVariable Long id) {
        return empleadoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Empleado> crear(@jakarta.validation.Valid @RequestBody Empleado empleado) {
        Empleado creado = empleadoService.guardar(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public Empleado actualizar(@PathVariable Long id, @jakarta.validation.Valid @RequestBody Empleado empleado) {
        return empleadoService.actualizar(id, empleado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Consultas personalizadas
    @GetMapping("/departamento/{nombre}")
    public List<Empleado> obtenerPorDepartamento(@PathVariable String nombre) {
        return empleadoService.buscarPorDepartamentoNombre(nombre);
    }

    @GetMapping("/salario")
    public List<Empleado> obtenerPorRangoSalario(@RequestParam BigDecimal min,
                                                 @RequestParam BigDecimal max) {
        return empleadoService.buscarPorRangoSalario(min, max);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> obtenerPorEmail(@PathVariable String email) {
        return empleadoService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
