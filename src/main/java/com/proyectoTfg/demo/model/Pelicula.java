package com.proyectoTfg.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="peliculas")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Pelicula;
    private String titulo;
    private String descripcion;
    private String genero;
    private Integer ano;
    @Lob
    @Column(name = "imagen", columnDefinition = "LONGTEXT")
    private String imagen;


    private Integer disponible;
    private Integer disponibleAlquiler;
    private String formato;
    private String trailer;


    @OneToMany(mappedBy = "pelicula")
    private List<UsuariosPeliculas> usuarios;

}
