package com.proyectoTfg.demo.controller;


import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.service.EmailService;
import com.proyectoTfg.demo.service.UsuarioService;
import com.proyectoTfg.demo.util.HashPassword;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class LoginController {

    @Autowired
    UsuarioService usuarioService;


    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model , HttpSession session) {

        Usuario usuario = usuarioService.findUsuarioByEmail(email);
        if(usuario !=null) {
            if (HashPassword.checkPassword(password, usuario.getPassword())) {
                session.setAttribute("usuario",usuario);
                return "redirect:/home/personal";

            }else{
                model.addAttribute("error","Contraseña incorrecta");
                return "login";

            }
        }
        model.addAttribute("error","Usuario No encontrado");
        return "login";
    }
}
