package com.proyectoTfg.demo.service;

import com.proyectoTfg.demo.model.Alquileres;
import com.proyectoTfg.demo.model.EstadosPeliculas;
import com.proyectoTfg.demo.repository.AlquileresRepository;
import com.proyectoTfg.demo.repository.EstadosPeliculasRepository;
import com.proyectoTfg.demo.repository.PeliculaRepository;
import com.proyectoTfg.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EstadosPeliculasService {

    @Autowired
    EstadosPeliculasRepository estadosPeliculasRepository;
    @Autowired
    PeliculaRepository peliculaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;




    public void estado(Integer idUsuario , Integer idPelicula, String estado){

        Optional<EstadosPeliculas> optional = estadosPeliculasRepository.findByUsuario_Id_UsuarioAndPelicula_Id(idUsuario, idPelicula);
        EstadosPeliculas actual;

        if(optional.isPresent()){
           actual = optional.get();
           actual.setEstado(estado);
           actual.setUltimoEstado(LocalDateTime.now().toString());
        }else{
            actual = new EstadosPeliculas();
            actual.setPelicula(peliculaRepository.findById(idPelicula).orElse(null));
            actual.setUsuario(usuarioRepository.findById(idUsuario).orElse(null));
            actual.setEstado(estado);
            actual.setDevuelta("false");

        }

        estadosPeliculasRepository.save(actual);

    }


    //filtro estado vista

    public List<EstadosPeliculas> fitlroVista(Integer idUsuario){
        return  estadosPeliculasRepository.filtroVista(idUsuario);

    }

    //Filtro pendiente
    public List<EstadosPeliculas> fitlroPendiente(Integer idUsuario){
        return  estadosPeliculasRepository.filtroPendiente(idUsuario);

    }
    //Filtro Posesion
    public List<EstadosPeliculas> filtroPosesion(Integer idUsuario){
        return  estadosPeliculasRepository.filtroPosesion(idUsuario);

    }




}
