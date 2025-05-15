package com.proyectoTfg.demo.service;

import com.proyectoTfg.demo.model.Alquileres;
import com.proyectoTfg.demo.model.Compra;
import com.proyectoTfg.demo.model.Pelicula;
import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.repository.CompraRepository;
import com.proyectoTfg.demo.repository.PeliculaRepository;
import com.proyectoTfg.demo.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompraService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;
    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private EmailService emailService;  // Inyectar el servicio de correo


    public void compra(Integer idUsuario, Integer idPelicula , RedirectAttributes redirectAttributes , HttpSession session){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        Pelicula pelicula = peliculaRepository.findById(idPelicula).orElse(null);

        Compra compra = compraRepository.UsuarioYPelicula(idUsuario, idPelicula).orElse(null);



        if(compra == null){
            compra = new Compra();
            compra.setUsuario(usuario);
            compra.setPelicula(pelicula);
            compra.setFechaCompra(LocalDate.now());

        }
        if("comprado".equals(compra.getEstadoCompra())){
            redirectAttributes.addFlashAttribute("error", "Ya has comprado esta pelicula");
            return;
        }

        //0 no hay stok  1 esta disponible
        if(pelicula.getDisponible()<=0){
            redirectAttributes.addFlashAttribute("error", "No tenemos stock actualmente");
            return;
        }
        pelicula.setDisponible(pelicula.getDisponible()-1);
        peliculaRepository.save(pelicula);

        if(usuario.getCredito() != null && usuario.getCredito()>=compra.getPrecioCompra()){

            double credito = usuario.getCredito()- compra.getPrecioCompra();
            usuario.setCredito(credito);

            session.setAttribute("usuario", usuario); //actualizacion pantalla
            usuarioRepository.save(usuario);

            compra.setEstadoCompra("comprado");
            compra.setFechaCompra(LocalDate.now());
            compraRepository.save(compra);


            emailService.compraCorreo(
                    usuario.getEmail(),
                    "Confirmación de Compra de película",
                    "¡Hola " + usuario.getNombre() + "!\n\n" +
                            "Tu Compra de la película '" + pelicula.getTitulo() + "' ha sido procesado con éxito.\n" +
                            "Precio de la compra: " + compra.getPrecioCompra() +"€"+ "\n\n" +
                            "Puedes venir a recogerla en cualquier momento.\n\n" +
                            "Gracias por usar nuestro servicio."
            );
            redirectAttributes.addFlashAttribute("correcto", "Compra realizada con éxito y correo enviado.");

        }else{
            redirectAttributes.addFlashAttribute("error", "Saldo insuficiente.");
        }

    }


    public List<Compra> filtroComprada(Integer idUsuario){
        return compraRepository.filtroComprada(idUsuario);

    }


}
