package com.proyectoTfg.demo.controller;



import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {

    @Autowired
    UsuarioService usuarioService;

    //VISTAS
    @GetMapping("/index")
    public String index(){
        return "index";

    }

    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null && "ADMIN".equals(usuario.getRol());
    }
    @GetMapping("/admin")
    public String admin(HttpSession session){
        if(!esAdmin(session)){
            return "redirect:/api/login";
        }
        return "admin";
    }









}
