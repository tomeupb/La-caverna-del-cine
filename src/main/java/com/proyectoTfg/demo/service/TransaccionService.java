package com.proyectoTfg.demo.service;



import com.proyectoTfg.demo.model.Transaccion;
import com.proyectoTfg.demo.model.Usuario;

import com.proyectoTfg.demo.repository.PeliculaRepository;
import com.proyectoTfg.demo.repository.TransaccionRepository;
import com.proyectoTfg.demo.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Service
public class TransaccionService {

    @Autowired
    UsuarioRepository usuarioRepository;


    @Autowired
    TransaccionRepository transaccionRepository;

    @Autowired
    private EmailService emailService;

    public void ingreso(Integer idUsuario, Double ingreso){

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);



        if (usuario.getCredito() == null) {
            usuario.setCredito(0.0);
        }

        Transaccion transaccion = new Transaccion();

        transaccion.setUsuario(usuario);
        transaccion.setFechaTransaccion(LocalDateTime.now().toString());
        transaccion.setIngreso(ingreso);

        transaccion.setMontoTotal(usuario.getCredito()+ingreso);

        Double nuevoCredito = usuario.getCredito() + ingreso;
        usuario.setCredito(nuevoCredito);

        transaccionRepository.save(transaccion);

        usuarioRepository.save(usuario);


        usuario.setCredito(nuevoCredito);
        emailService.ingresoCorreo(
                usuario.getEmail(),
                "Confirmación de ingreso de saldo",
                "¡Hola " + usuario.getNombre() + "!\n\n" +
                        "Tu ingreso de saldo ha sido procesado con éxito.\n" +
                        "Cantidad ingresada:" + ingreso +"€"+ "\n" +
                        "Saldo actual: €" +  nuevoCredito+"€"+ "\n\n" +
                        "Gracias por usar nuestro servicio."
        );



    }

    public void deuda(Integer idUsuario, Double pagoDeuda ,  RedirectAttributes redirectAttributes){

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

            usuario.setDeuda((int)(usuario.getDeuda()-pagoDeuda));
            usuario.setCredito(usuario.getCredito()-pagoDeuda);

        usuarioRepository.save(usuario);



    }
}
