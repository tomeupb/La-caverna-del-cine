package com.proyectoTfg.demo.controller;


import com.proyectoTfg.demo.model.*;

import com.proyectoTfg.demo.repository.AlquileresRepository;
import com.proyectoTfg.demo.repository.UsuariosPeliculasRepository;
import com.proyectoTfg.demo.service.AlquilerService;
import com.proyectoTfg.demo.service.CompraService;
import com.proyectoTfg.demo.service.PeliculaService;
import com.proyectoTfg.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
@RequestMapping("/home")
public class PersonalController {

        @Autowired
        PeliculaService peliculaService;

    @Autowired
    AlquilerService alquilerService;
    @Autowired
    private UsuariosPeliculasRepository usuariosPeliculasRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    private CompraService compraService;


    @GetMapping("personal")
    public String personal(HttpSession session , Model model) {

        Usuario usuario = (Usuario)session.getAttribute("usuario");

        if(usuario ==null){ //mostrar por pantalla la session del usuario al logearse
            return "redirect:/api/login";
        }
       // if(session.getAttribute("usuario")==null){ //si la session es nula que devuelva al login
       //     return "redirect:/api/login";
       // }
        List<Alquileres>listaAlquiler = alquilerService.listaAlquileres(usuario.getId_Usuario());
        List<Compra>listaCompra = compraService.filtroComprada(usuario.getId_Usuario());

        model.addAttribute("usuario",usuario);
        model.addAttribute("alquileres",listaAlquiler);
        model.addAttribute("compras",listaCompra);


        return "personal";
    }

    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null && "ADMIN".equals(usuario.getRol());
    }

    @GetMapping("/listaPersonal")
    public String listaPeliculas(Model model , HttpSession session) {


        Usuario usuario = (Usuario)session.getAttribute("usuario");

        Integer idUsuario = usuario.getId_Usuario();

        List<Pelicula> peliculas = peliculaService.getAllPeliculas();
        model.addAttribute("usuario", usuario);
        model.addAttribute("peliculas", peliculas);

        return "listaPersonal";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/api/login";
    }



    @GetMapping("/ingresar")
    public String ingresar(){
        return "/ingresar";
    }




    }
