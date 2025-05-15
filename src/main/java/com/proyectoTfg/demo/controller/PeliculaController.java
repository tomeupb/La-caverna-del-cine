package com.proyectoTfg.demo.controller;


import com.proyectoTfg.demo.model.EstadosPeliculas;
import com.proyectoTfg.demo.model.Pelicula;
import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.model.UsuariosPeliculas;
import com.proyectoTfg.demo.repository.PeliculaRepository;
import com.proyectoTfg.demo.repository.TransaccionRepository;
import com.proyectoTfg.demo.service.EstadosPeliculasService;
import com.proyectoTfg.demo.service.PeliculaService;
import com.proyectoTfg.demo.service.UsuarioService;
import com.proyectoTfg.demo.service.UsuariosPeliculasService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/pelicula")
public class PeliculaController {

    @Autowired
    PeliculaService peliculaService;

    @Autowired
    UsuariosPeliculasService usuariosPeliculasService;

    @Autowired
    EstadosPeliculasService estadosPeliculasService;

    @Autowired
    PeliculaRepository peliculaRepository;

    @Autowired
    TransaccionRepository transaccionRepository;
    @Autowired
    private UsuarioService usuarioService;


    //Verificar si eres admin
    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null && "ADMIN".equals(usuario.getRol());
    }


    //Vista adminPeliculas
    @GetMapping("/adminPeliculas")
    public String adminPeliculas(HttpSession session) {
        if(!esAdmin(session)){
            return "redirect:/api/login";
        }
        return "adminPeliculas";
    }


    //Vista Lista de peliculas admin
    @GetMapping("/listaPeliculas")
    public String listaPeliculas(Model model, HttpSession session) {

        if(!esAdmin(session)){
            return "redirect:/api/login";
        }
        List<Pelicula> peliculas =  peliculaService.getAllPeliculas();
        model.addAttribute("peliculas", peliculas);
        return "listaPeliculas";
    }


    //lista para personal
    @GetMapping("/listaPersonal")
    public String listaPersonal(Model model, HttpSession session) {


        List<Pelicula> peliculas =  peliculaService.getAllPeliculas();

        Usuario usuario= (Usuario)session.getAttribute("usuario");
        Integer IdUsuario = usuario.getId_Usuario();
        List<UsuariosPeliculas>usuariosPeliculas = usuariosPeliculasService.obtenerTodas(IdUsuario);

        Map<Integer, UsuariosPeliculas> mapaUsuariosPeliculas = new HashMap<>();
        for (UsuariosPeliculas up : usuariosPeliculas) {
            mapaUsuariosPeliculas.put(up.getPelicula().getId_Pelicula(), up);
            model.addAttribute("mapaUsuariosPeliculas", mapaUsuariosPeliculas);// Relacionamos la película con su calificación y estado favorito
        }
        model.addAttribute("usuariosPeliculas", usuariosPeliculas);
        model.addAttribute("peliculas", peliculas);
        return "listaPersonal";
    }



    //Borrar peliculas por id

    @PostMapping("/borrar")
    public String deletePelicula(@RequestParam Integer id_Pelicula , Model model, RedirectAttributes redirectAttributes){

        if(peliculaRepository.findById(id_Pelicula).isEmpty()){
            redirectAttributes.addFlashAttribute("error","Pelicula no encontrada para Eliminar");
            return "redirect:/pelicula/adminPeliculas";
        }
        peliculaService.deletePelicula(id_Pelicula);
        redirectAttributes.addFlashAttribute("correcto","Pelicula borrada correctamente");
        return "redirect:/pelicula/listaPeliculas";
    }




    @PostMapping("/actualizar")
    public String updatePelicula(@RequestParam("titulo") String titulo,
                                 @RequestParam("id_Pelicula") Integer id_Pelicula,
                              @RequestParam("descripcion") String descripcion,
                              @RequestParam("genero") String genero,
                              @RequestParam("ano") Integer ano,
                              @RequestParam("imagen") MultipartFile imagen,
                              @RequestParam("disponible") Integer disponible,
                              @RequestParam("trailer") String trailer,
                              @RequestParam("formato") String formato,
                              @RequestParam("disponibleAlquiler") Integer disponibleAlquiler,
                              Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Convertir imagen a Base64

            if(peliculaRepository.findById(id_Pelicula).isEmpty()){
                redirectAttributes.addFlashAttribute("error","Pelicula no encontrada para actualizar");
                return "redirect:/pelicula/adminPeliculas";

            }
            String base64Imagen = Base64.getEncoder().encodeToString(imagen.getBytes());
            Pelicula pelicula = new Pelicula();
            pelicula.setId_Pelicula(id_Pelicula);
            pelicula.setTitulo(titulo);
            pelicula.setDescripcion(descripcion);
            pelicula.setGenero(genero);
            pelicula.setAno(ano);
            pelicula.setImagen(base64Imagen);
            pelicula.setFormato(formato);

            pelicula.setTrailer(trailer);
            pelicula.setDisponible(disponible);
            pelicula.setDisponibleAlquiler(disponibleAlquiler);

            peliculaService.addPelicula(pelicula);
            model.addAttribute("pelicula", pelicula);
            redirectAttributes.addFlashAttribute("correcto","Pelicula Actualizada correctamente");
            return "redirect:/pelicula/listaPeliculas";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }




    @PostMapping("/crear")
    public String addPelicula(@RequestParam("titulo") String titulo,
                              @RequestParam("descripcion") String descripcion,
                              @RequestParam("genero") String genero,
                              @RequestParam("ano") Integer ano,
                              @RequestParam("imagen") MultipartFile imagen,
                              @RequestParam("disponible") Integer disponible,
                              @RequestParam("trailer") String trailer,
                              @RequestParam("formato") String formato,
                              @RequestParam("disponibleAlquiler") Integer disponibleAlquiler,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            // Convertir imagen a Base64
            String base64Imagen = Base64.getEncoder().encodeToString(imagen.getBytes());

            // Crear y guardar la película
            Pelicula pelicula = new Pelicula();
            pelicula.setTitulo(titulo);
            pelicula.setDescripcion(descripcion);
            pelicula.setGenero(genero);
            pelicula.setAno(ano);
            pelicula.setImagen(base64Imagen);
            pelicula.setFormato(formato);

            pelicula.setTrailer(trailer);
            pelicula.setDisponible(disponible);
            pelicula.setDisponibleAlquiler(disponibleAlquiler);

            peliculaService.addPelicula(pelicula);
            model.addAttribute("pelicula", pelicula);
            redirectAttributes.addFlashAttribute("correcto","Pelicula creada correctamente");
            return "redirect:/pelicula/listaPeliculas";
        } catch (IOException e) {
            return "Error al procesar la imagen: " + e.getMessage();
        }
    }



















    //Calificar una pelicula

    @PostMapping("/calificar")
    public String calificarPelicula(@RequestParam Integer idPelicula, @RequestParam Integer calificacion, HttpSession session, RedirectAttributes redirectAttributes) {
        // Obtener el usuario desde la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        //List<Pelicula> pelicula = peliculaService.getAllPeliculas();

        Pelicula pelicula = peliculaRepository.findById(idPelicula).orElse(null);

        if (usuario == null) {
            System.out.println("session no encontrada metodo calificar");
            return "redirect:/api/login";
        }
        // Llamar al servicio para calificar la película
        usuariosPeliculasService.calificar(usuario.getId_Usuario(), idPelicula, calificacion,redirectAttributes);
        redirectAttributes.addFlashAttribute("correcto","Pelicula "+pelicula.getTitulo()+" calificada con: " +calificacion+ " realizada con éxito");

        // Redirigir a la página de lista de películas después de calificar
        return "redirect:/home/listaPersonal";
    }


    //Marcar como favorita
    @PostMapping("/favorita")
    public String favorita(@RequestParam Integer idPelicula,
                           @RequestParam(required = false) String favorita ,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            System.out.println("session no encontrada metodo favorito");
            return "redirect:/api/login";
        }
        if (favorita == null || !favorita.equals("true")) {
            favorita = "true";  // Si no está marcado o es null, marcarlo como "true"
        } else {
            favorita = "false";  // Si está marcado, cambiarlo a "false"
        }

        //redirectAttributes.addFlashAttribute("correcto","Pelicula Marcada como favorita");
        usuariosPeliculasService.favorita(usuario.getId_Usuario(),idPelicula, favorita ,redirectAttributes);

        return "redirect:/home/listaPersonal";
    }




    //Filtrar por peliculas favoritas

    @GetMapping("filtroFavoritas")
    public String obtenerFavoritos(RedirectAttributes redirectAttributes, HttpSession session, Model model) {


        Usuario usuario = (Usuario)session.getAttribute("usuario");

        Integer idUsuario = usuario.getId_Usuario();

        List<UsuariosPeliculas> lista = usuariosPeliculasService.obtenerFavoritos(idUsuario);

        List<Pelicula>peliculas = lista.stream()
                .map(UsuariosPeliculas::getPelicula)
                .collect(Collectors.toList());


        if(peliculas.isEmpty()){
            redirectAttributes.addFlashAttribute("error","No tienes peliculas favoritas");
            return "redirect:/pelicula/listaPersonal";
        }

        model.addAttribute("peliculas",peliculas);
        return "listaPersonal";
    }



    @GetMapping("calificacionAsc")
    public String calificacionAsc(Model model , HttpSession session ,RedirectAttributes redirectAttributes){
        Usuario usuario = (Usuario)session.getAttribute("usuario");

        if (usuario == null) {
            System.out.println("session perdida en calificacionAsc"); // Si no hay idUsuario, redirigir al login
            return "redirect:/api/login";
        }
        Integer idUsuario = usuario.getId_Usuario();
        List<UsuariosPeliculas>lista = usuariosPeliculasService.calificacionAsc(idUsuario);

        List<Pelicula> peliculas = lista.stream()
                .map(UsuariosPeliculas::getPelicula)
                .collect(Collectors.toList());

        if(peliculas.isEmpty()){
            redirectAttributes.addFlashAttribute("error","No tienes peliculas calificadas");
            return "redirect:/pelicula/listaPersonal";
        }

        model.addAttribute("peliculas",peliculas);
        return "listaPersonal";
    }




    //guardar el estado que seleccionan vista-pendiente-posesion
    @PostMapping("/estado")
    public String estado(HttpSession session , @RequestParam Integer idPelicula, @RequestParam String estado,RedirectAttributes redirectAttributes){

        Usuario usuario =(Usuario)session.getAttribute("usuario");
        Integer idUsuario = usuario.getId_Usuario();

        redirectAttributes.addFlashAttribute("correcto","Pelicula Marcada como "+estado);
        estadosPeliculasService.estado(idUsuario,idPelicula,estado);
        return "redirect:/home/listaPersonal";

    }



    @GetMapping("/filtroVista")
    public String filtroVista(Model model , HttpSession session ,RedirectAttributes redirectAttributes){

        Usuario usuario = (Usuario)session.getAttribute("usuario");


        Integer idUsuario = usuario.getId_Usuario();

        List<EstadosPeliculas> lista = estadosPeliculasService.fitlroVista(idUsuario);

        List<Pelicula>peliculas = lista.stream()
                .map(EstadosPeliculas::getPelicula)
                .collect(Collectors.toList());

        if(peliculas.isEmpty()){
            redirectAttributes.addFlashAttribute("error","No tienes peliculas vistas");
            return "redirect:/pelicula/listaPersonal";
        }
        model.addAttribute("peliculas",peliculas);
        return "listaPersonal";


    }

    @GetMapping("/filtroPendiente")
    public String filtroPendiente(Model model , HttpSession session,RedirectAttributes redirectAttributes){

        Usuario usuario = (Usuario)session.getAttribute("usuario");


        Integer idUsuario = usuario.getId_Usuario();

        List<EstadosPeliculas> lista = estadosPeliculasService.fitlroPendiente(idUsuario);

        List<Pelicula>peliculas = lista.stream()
                .map(EstadosPeliculas::getPelicula)
                .collect(Collectors.toList());


        if(peliculas.isEmpty()){
            redirectAttributes.addFlashAttribute("error","No tienes peliculas pendientes");
            return "redirect:/pelicula/listaPersonal";
        }
        model.addAttribute("peliculas",peliculas);
        return "listaPersonal";


    }

    @GetMapping("/filtroPosesion")
    public String filtroPosesion(Model model , HttpSession session,RedirectAttributes redirectAttributes){

        Usuario usuario = (Usuario)session.getAttribute("usuario");
        Integer idUsuario = usuario.getId_Usuario();

        List<EstadosPeliculas> lista = estadosPeliculasService.filtroPosesion(idUsuario);

        List<Pelicula>peliculas = lista.stream()
                .map(EstadosPeliculas::getPelicula)
                .collect(Collectors.toList());

        if(peliculas.isEmpty()){
            redirectAttributes.addFlashAttribute("error","No tienes peliculas en posesión");
            return "redirect:/pelicula/listaPersonal";
        }

        model.addAttribute("peliculas",peliculas);
        return "listaPersonal";


    }


    //Estadisticas

    @GetMapping("/estadisticas")
    public String estadisticas(){
        return "estadisticas";

    }

    @PostMapping("/estadisticas")
    public String peliculasVistasPorGenero(@RequestParam String genero,
                                       @RequestParam String sexo,
                                       Model model) {

        Long total = usuariosPeliculasService.peliculasVistasPorGenero(genero, sexo);
        System.out.println("Género: " + genero);
        System.out.println("Sexo: " + sexo);
        System.out.println("Total películas vistas: " + total);
        model.addAttribute("genero", genero);
        model.addAttribute("sexo", sexo);
        model.addAttribute("total", total);
        return "estadisticas";
    }


    @PostMapping("/comprasPorSexo")
    public String comprasPorSexo(@RequestParam String sexo,
                                           Model model) {
        Long total2 = usuariosPeliculasService.comprasPorSexo(sexo);
        model.addAttribute("sexo", sexo);
        model.addAttribute("total2", total2);
        return "estadisticas";
    }

    @PostMapping("/alquilerPorSexo")
    public String alquilerPorSexo(@RequestParam String sexo,
                                 Model model) {
        Long total3 = usuariosPeliculasService.alquilerPorSexo(sexo);
        model.addAttribute("sexo", sexo);
        model.addAttribute("total3", total3);
        return "estadisticas";
    }

    @PostMapping("/calificacionesGenero")
    public String calificacionesGenero(@RequestParam String genero,
                                  Model model) {
        Long total4 = usuariosPeliculasService.calificacionesGenero(genero);
        model.addAttribute("sexo", genero);
        model.addAttribute("total4", total4);
        return "estadisticas";
    }

    @PostMapping("/sumaTotalIngresos")
    public String sumaTotalIngresos(@RequestParam Integer idUsuario , Model model){


        Integer total5 = transaccionRepository.sumaTotalIngresos(Long.valueOf(idUsuario));

      /**/
        model.addAttribute("total5",total5);
        return "estadisticas";



    }








}

