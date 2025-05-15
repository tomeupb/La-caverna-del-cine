package com.proyectoTfg.demo.controller;


import com.proyectoTfg.demo.model.Pelicula;
import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.repository.UsuarioRepository;
import com.proyectoTfg.demo.service.PeliculaService;
import com.proyectoTfg.demo.service.UsuarioService;
import com.proyectoTfg.demo.util.HashPassword;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    private PeliculaService peliculaService;
    @Autowired
    private UsuarioRepository usuarioRepository;


    //Verificar si eres admin
    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null && "ADMIN".equals(usuario.getRol());
    }


    @GetMapping("/adminUsers")
    public String adminUsers(HttpSession session){

        if(!esAdmin(session)){
            return "redirect:/api/login";
        }
        return "adminUsers";
    }

    //CREAR
    @PostMapping("adminRegister")
    public String adminRegister(Usuario usuario, RedirectAttributes redirectAttributes) {

        if(usuarioService.uniqueEmail(usuario.getEmail())){
            //hashear tambien la contraseña cuando un admin la crea
            String hashedPassword = HashPassword.hashPassword(usuario.getPassword());
            usuario.setPassword(hashedPassword);
            usuarioService.addUsuario(usuario);
            redirectAttributes.addFlashAttribute("success","Usuario Creado con éxito");
            return "redirect:/usuario/listaUsuarios";
            //return "listaUsuarios";
        }else{
            redirectAttributes.addFlashAttribute("error", "El email tiene que ser unico");
            //return "redirect:/usuario/listaUsuarios";
            return "redirect:/usuario/listaUsuarios";
        }

    }

    //Eliminar
   @PostMapping("/adminDelete")
    public String adminDelete(@RequestParam Integer id_Usuario , RedirectAttributes redirectAttributes) {


       Usuario usuarios = usuarioService.findById(id_Usuario);

       if (usuarios != null) {
           usuarioService.adminDelete(id_Usuario);
           redirectAttributes.addFlashAttribute("usuario", usuarios);
           redirectAttributes.addFlashAttribute("success","Usuario Eliminado con éxito");
           return "redirect:/usuario/listaUsuarios";
       }
       redirectAttributes.addFlashAttribute("error", "No puedes borrar una id que no existe");
       return "redirect:/usuario/listaUsuarios";


    }




    //Actualizar
    @PostMapping("/adminUpdate")
    public String adminUpdate(Usuario usuario, RedirectAttributes redirectAttributes){

        Optional<Usuario>actual = usuarioRepository.findById(usuario.getId_Usuario());

        if(actual.isEmpty()){
            redirectAttributes.addFlashAttribute("error","Usuario no encontrado para actualizar");
            return "redirect:/usuario/listaUsuarios";
        }
        String hashedPassword = HashPassword.hashPassword(usuario.getPassword());
        usuario.setPassword(hashedPassword);
       usuarioService.adminUpdate(usuario);
       return "redirect:/usuario/listaUsuarios";
    }

    //listar
    @GetMapping("listaUsuarios")
    public String listaUsuarios(Model model ,HttpSession session) {

        if(!esAdmin(session)){
            return "redirect:/api/login";
        }

        List <Usuario> usuarios = usuarioService.adminGetAll();
        List<Pelicula>peliculas = peliculaService.getAllPeliculas();
        model.addAttribute("peliculas",peliculas);
        model.addAttribute("usuarios",usuarios);
        if(usuarios != null ){

            //model.addAttribute("success","Lista obtenida");
            return "listaUsuarios";
        }else{
            model.addAttribute("error", "La lista esta vacia no hay usuarios");
            return "listaUsuarios";
        }

    }

    //Filtrar por email

    @GetMapping("/adminFindEmail")
    public String findUsuarioByEmail(@RequestParam String email, Model model){

        Usuario usuarios = usuarioService.findUsuarioByEmail(email);

        if(usuarios != null){
            model.addAttribute("usuarios",usuarios);
            model.addAttribute("success","Usuario encontrado por Email con éxito");
            return "listaUsuarios";
        }else{
            model.addAttribute("error", "Usuario no encontrado con ese email");
            return "listaUsuarios";
        }

    }

    //Filtrar por id

    @GetMapping("/adminFindId")
    public String findById(@RequestParam Integer id_Usuario , Model model){

        Usuario usuarios = usuarioService.findById(id_Usuario);
Optional<Usuario> encontrado = usuarioRepository.findById(id_Usuario);
        if(encontrado.isPresent()){
            model.addAttribute("usuarios",usuarios);
            model.addAttribute("success","Usuario encontrado por ID con éxito");
            return "listaUsuarios";
        }
        model.addAttribute("error","Usuario con esa Id no Encontrado");
        return "listaUsuarios";
    }







}












