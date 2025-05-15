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
@Table(name = "usuarios")
public class Usuario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id_Usuario;
    private String nombre;
    @Column(unique = true)
    private String email;
    private String password;
    private String rol = "USER";
    private Double credito=0.0;
    private Integer deuda = 0;
    private String sexo;


    @OneToMany(mappedBy = "usuario")
    private List<UsuariosPeliculas> peliculas;



}
