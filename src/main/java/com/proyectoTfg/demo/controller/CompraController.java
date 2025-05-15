package com.proyectoTfg.demo.controller;

import com.proyectoTfg.demo.model.Compra;
import com.proyectoTfg.demo.model.Pelicula;
import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.repository.CompraRepository;
import com.proyectoTfg.demo.service.CompraService;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("compra")
public class CompraController {


    private final CompraRepository compraRepository;
    private final CompraService compraService;

    public CompraController(CompraRepository compraRepository, CompraService compraService) {
        this.compraRepository = compraRepository;
        this.compraService = compraService;
    }

    @PostMapping("/permanente")
    public String compra(@RequestParam Integer idPelicula,
                         RedirectAttributes redirectAttributes,
                         HttpSession session){

        Usuario usuario = (Usuario)session.getAttribute("usuario");

        Integer idUsuario = usuario.getId_Usuario();
        compraService.compra(idUsuario, idPelicula, redirectAttributes,session);

        return "redirect:/home/listaPersonal";
    }


    @GetMapping("/filtroComprada")
    public  String filtroComprada(Model model , HttpSession session,RedirectAttributes redirectAttributes){


        Usuario usuario = (Usuario) session.getAttribute("usuario");

        Integer idUsuario = usuario.getId_Usuario();

        List<Compra> lista = compraRepository.filtroComprada(idUsuario);

        List<Pelicula> peliculas = lista.stream()
                .map(Compra::getPelicula)
                .collect(Collectors.toList());

        if(peliculas.isEmpty()){
            redirectAttributes.addFlashAttribute("error","No tienes peliculas compradas");
            return "redirect:/pelicula/listaPersonal";
        }

        model.addAttribute("peliculas",peliculas);

        return "listaPersonal";


    }

}
