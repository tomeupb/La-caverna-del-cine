package com.proyectoTfg.demo.controller;

import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.service.EmailService;
import com.proyectoTfg.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import  com.proyectoTfg.demo.util.*;

@Controller
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    private EmailService emailService;

    @GetMapping("register")
    public String register(Usuario usuario , Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("register")
    public String registerP(Usuario usuario , Model model) {

        if (usuarioService.uniqueEmail(usuario.getEmail())) {

            String hashedPassword = HashPassword.hashPassword(usuario.getPassword());
            usuario.setPassword(hashedPassword);
            usuarioService.addUsuario(usuario);
            emailService.registerCorreo(
                    usuario.getEmail(),
                    "Creacion de cuenta en La Caverna del Cine ",
                    "¡Hola " + usuario.getNombre() + "!\n\n" +
                            "¡Bienvenido/a a La Caverna del Cine!\n" +
                            "Nos alegra que te hayas registrado con nosotros.\n\n" +
                            "Detalles de tu cuenta:\n" +
                            "Nombre: " + usuario.getNombre() + "\n" +
                            "Correo: " + usuario.getEmail() + "\n\n" +
                            "Tu cuenta ha sido creada con éxito y ya puedes comenzar a disfrutar de nuestros servicios.\n" +
                            "Gracias por confiar en nosotros.\n" +
                            "El equipo de La Caverna del Cine");

            return "redirect:/api/login";
        } else {

            model.addAttribute("error", "Cuenta ya registrada con este email , intentalo con otro");
            return "register";

        }



    }




}
