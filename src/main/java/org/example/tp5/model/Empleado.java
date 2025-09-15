package org.example.tp5.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @jakarta.validation.constraints.NotBlank
    private String nombre;

    @Column(nullable = false, length = 100)
    @jakarta.validation.constraints.NotBlank
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Email
    private String email;

    @Column(name = "fecha_contratacion", nullable = false)
    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.PastOrPresent
    private LocalDate fechaContratacion;

    @Column(nullable = false, precision = 12, scale = 2)
    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.PositiveOrZero
    private BigDecimal salario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    @jakarta.validation.constraints.NotNull
    private Departamento departamento;

    @ManyToMany
    @JoinTable(
            name = "empleado_proyecto",
            joinColumns = @JoinColumn(name = "empleado_id"),
            inverseJoinColumns = @JoinColumn(name = "proyecto_id")
    )
    private Set<Proyecto> proyectos = new HashSet<>();

    public Empleado() {
    }

    public Empleado(Long id, String nombre, String apellido, String email, LocalDate fechaContratacion, BigDecimal salario, Departamento departamento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
        this.departamento = departamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Set<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(Set<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }
}
