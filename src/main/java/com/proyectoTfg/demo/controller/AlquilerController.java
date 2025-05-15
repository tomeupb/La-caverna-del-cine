package com.proyectoTfg.demo.controller;

import com.proyectoTfg.demo.model.Alquileres;
import com.proyectoTfg.demo.model.Pelicula;
import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.service.AlquilerService;
import com.proyectoTfg.demo.service.PeliculaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller

@RequestMapping("alquiler")
public class AlquilerController {

    @Autowired
    AlquilerService alquilerService;

    @PostMapping("/temporal")
    public String temporal(@RequestParam  Integer idPelicula,
                           @RequestParam String estadoAlquiler,
                           HttpSession session,
                           RedirectAttributes redirectAttributes){

        Usuario usuario = (Usuario)session.getAttribute("usuario");

        Integer idUsuario = usuario.getId_Usuario();



        if("devuelta".equals(estadoAlquiler)){
            alquilerService.devuelta(idUsuario,idPelicula,redirectAttributes);

        } else if ("alquilada".equals(estadoAlquiler)) {

            alquilerService.alquilada(idUsuario, idPelicula, estadoAlquiler, redirectAttributes);

        }

        return "redirect:/home/listaPersonal";


    }


    @PostMapping("/devolver")
    public String devolver(@RequestParam  Integer idPelicula,
                           @RequestParam  Integer idUsuario,
                           @RequestParam String estadoAlquiler,
                           HttpSession session,
                           RedirectAttributes redirectAttributes){

        if ("devuelta".equals(estadoAlquiler)) {
            alquilerService.devuelta(idUsuario, idPelicula, redirectAttributes);
        } else if ("alquilada".equals(estadoAlquiler)) {
            alquilerService.alquilada(idUsuario, idPelicula, estadoAlquiler, redirectAttributes);
        }

        return "redirect:/alquiler/gestion";

    }



    @GetMapping("/filtroAlquilada")
    public String filtroAlquilada(Model model , HttpSession session,RedirectAttributes redirectAttributes){

        Usuario usuario = (Usuario)session.getAttribute("usuario");
        Integer idUsuario = usuario.getId_Usuario();
        List<Alquileres> lista = alquilerService.filtroAlquilada(idUsuario);

        List<Pelicula>peliculas = lista.stream()
                .map(Alquileres::getPelicula)
                .collect(Collectors.toList());

        model.addAttribute("peliculas",peliculas);
        if(peliculas.isEmpty()){
            redirectAttributes.addFlashAttribute("error","No tienes peliculas Alquiladas");
            return "redirect:/pelicula/listaPersonal";
        }

        return "listaPersonal";
    }

    //Verificar si eres admin
    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null && "ADMIN".equals(usuario.getRol());
    }


    @GetMapping("/gestion")
    public String verUsuariosConPeliculasAlquiladas(Model model , HttpSession session) {

        if(!esAdmin(session)){
            return "redirect:/api/login";
        }

        List<Alquileres> alquileres = alquilerService.obtenerAlquileresActivos();
        model.addAttribute("alquileres", alquileres);

        if (alquileres.isEmpty()) {
            model.addAttribute("error", "No hay películas alquiladas actualmente.");
        } else {
            //model.addAttribute("success", "Lista de películas alquiladas obtenida.");
        }

        return "gestion";
    }










}


