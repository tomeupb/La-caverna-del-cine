package com.proyectoTfg.demo.service;

import com.proyectoTfg.demo.model.Pelicula;
import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.model.UsuariosPeliculas;
import com.proyectoTfg.demo.repository.UsuariosPeliculasRepository;
import com.proyectoTfg.demo.repository.UsuarioRepository;
import com.proyectoTfg.demo.repository.PeliculaRepository;
import com.sun.jdi.connect.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuariosPeliculasService {

    @Autowired
    private UsuariosPeliculasRepository usuariosPeliculasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;

    public void calificar(Integer idUsuario, Integer idPelicula, Integer calificacion,RedirectAttributes redirectAttributes) {


        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        Pelicula pelicula = peliculaRepository.findById(idPelicula).orElse(null);

        if(calificacion>5 || calificacion<=0){
            redirectAttributes.addFlashAttribute("error", "Calificacion Válida del 1 al 5");

        }
        UsuariosPeliculas usuariosPeliculas = usuariosPeliculasRepository.findByUsuarioIdAndPeliculaId(idUsuario, idPelicula);
        if (usuariosPeliculas == null) {
            usuariosPeliculas = new UsuariosPeliculas();
            usuariosPeliculas.setUsuario(usuario);
            usuariosPeliculas.setPelicula(pelicula);
            usuariosPeliculas.setFechaCalificacion(LocalDateTime.now().toString());
        }

        usuariosPeliculas.setCalificacion(calificacion);
        usuariosPeliculasRepository.save(usuariosPeliculas);
    }



    public void favorita(Integer idUsuario, Integer idPelicula, String favorita, RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        Pelicula pelicula = peliculaRepository.findById(idPelicula).orElse(null);
        UsuariosPeliculas usuariosPeliculas = usuariosPeliculasRepository.findByUsuarioIdAndPeliculaId(idUsuario, idPelicula);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return;
        }

        if (usuariosPeliculas == null) {
            redirectAttributes.addFlashAttribute("error", "Califica primero la pelicula antes de marcarla como Favorito.");
            return;
        }

        if (usuario == null) {
            usuariosPeliculas = new UsuariosPeliculas();
            usuariosPeliculas.setUsuario(usuario);
            usuariosPeliculas.setPelicula(pelicula);
        }

        if (usuariosPeliculas.getFavorita().equals("true")) {
            usuariosPeliculas.setFavorita("false");
            usuariosPeliculas.setFechaFavorita(LocalDateTime.now().toString());
            redirectAttributes.addFlashAttribute("correcto","Pelicula Quitada como favorita");
            usuariosPeliculasRepository.save(usuariosPeliculas);
        }else{
            usuariosPeliculas.setFavorita("true");
            redirectAttributes.addFlashAttribute("correcto","Pelicula Marcada como favorita");

            usuariosPeliculas.setFechaFavorita(LocalDateTime.now().toString());
            usuariosPeliculasRepository.save(usuariosPeliculas);
        }


        }


        //filtro favoritos
        public List<UsuariosPeliculas> obtenerFavoritos(Integer idUsuario){
        return usuariosPeliculasRepository.filtroFavorito(idUsuario);

        }
        //fitro ver lista completa
        public List<UsuariosPeliculas> obtenerTodas(Integer idUsuario){
        return  usuariosPeliculasRepository.obtenerTodas(idUsuario);

        }

        //Filtrar lista con calificacion ascendente
        public List<UsuariosPeliculas> calificacionAsc(Integer idUsuario){
        return  usuariosPeliculasRepository.listaCalificadaAsc(idUsuario);

        }


        public Long peliculasVistasPorGenero(String genero, String sexo) {
        return usuariosPeliculasRepository.peliculasVistasPorGenero(genero, sexo);
        }

    public Long comprasPorSexo(String sexo) {
        return usuariosPeliculasRepository.comprasPorSexo(sexo);
    }

    public Long alquilerPorSexo(String sexo) {
        return usuariosPeliculasRepository.alquilerPorSexo(sexo);
    }

    public Long calificacionesGenero(String genero) {
        return usuariosPeliculasRepository.calificacionesGenero(genero);
    }













    }



