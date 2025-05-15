package com.proyectoTfg.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "alquileres")
public class Alquileres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alquiler")
    private  Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_pelicula")
    private Pelicula pelicula;


    @Column(name = "fecha_alquiler")
    private LocalDate fechaAlquiler;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;


    private  Double precioAlquiler=5.0;


    @Column(name = "estado_Alquiler")
    private String estadoAlquiler = "devuelta";



}
