package com.proyectoTfg.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuariosPeliculas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name ="id_usuario")
    private  Usuario usuario;

    @ManyToOne
    @JoinColumn(name ="id_pelicula")
    private Pelicula pelicula;

    private String fechaCalificacion;

    private Integer calificacion=0;

    private String favorita = "false";

    private String fechaFavorita;




}
