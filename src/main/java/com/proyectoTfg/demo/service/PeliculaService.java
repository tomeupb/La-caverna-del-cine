package com.proyectoTfg.demo.service;


import com.proyectoTfg.demo.model.Pelicula;
import com.proyectoTfg.demo.model.UsuariosPeliculas;
import com.proyectoTfg.demo.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeliculaService {


    @Autowired
    PeliculaRepository peliculaRepository;


    //crear
    public void addPelicula(Pelicula pelicula) {
            peliculaRepository.save(pelicula);
    }

    //listar
    public List<Pelicula> getAllPeliculas(){
        return  peliculaRepository.findAll();
    }
    //borrar
    public void deletePelicula(Integer id_Pelicula){
        peliculaRepository.deleteById(id_Pelicula);
    }

    //actualizar

    public void updatePelicula(Pelicula pelicula){
        peliculaRepository.save(pelicula);
    }












}
