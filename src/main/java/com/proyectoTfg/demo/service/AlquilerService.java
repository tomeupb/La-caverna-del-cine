package com.proyectoTfg.demo.service;

import com.proyectoTfg.demo.model.Alquileres;
import com.proyectoTfg.demo.model.EstadosPeliculas;
import com.proyectoTfg.demo.model.Pelicula;
import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.repository.AlquileresRepository;
import com.proyectoTfg.demo.repository.EstadosPeliculasRepository;
import com.proyectoTfg.demo.repository.PeliculaRepository;
import com.proyectoTfg.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Service
public class AlquilerService {

    @Autowired
    AlquileresRepository alquileresRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private EmailService emailService;  // Inyectar el servicio de correo

    @Autowired
   EstadosPeliculasRepository estadosPeliculasRepository;



    public void alquilada(Integer idUsuario, Integer idPelicula, String estadoAlquiler, RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        Pelicula pelicula = peliculaRepository.findById(idPelicula).orElse(null);

        Alquileres alquileres = alquileresRepository.findByUsuario_Id_UsuarioAndPelicula_Id(idUsuario, idPelicula).orElse(null);

        if (alquileres == null) {
            alquileres = new Alquileres();
            alquileres.setUsuario(usuario);
            alquileres.setPelicula(pelicula);
            alquileres.setGenero(pelicula.getGenero());
            alquileres.setFechaAlquiler(LocalDate.now());
        }

        if ("alquilada".equals(alquileres.getEstadoAlquiler())) {
            redirectAttributes.addFlashAttribute("error", "Ya has alquilado la pelicula.");
            return;

        }

        if(pelicula.getDisponibleAlquiler()<=0){
            redirectAttributes.addFlashAttribute("error", "No tenemos stock actualmente");
            return;
        }

        pelicula.setDisponibleAlquiler(pelicula.getDisponibleAlquiler()-1);
        peliculaRepository.save(pelicula);


        if (usuario.getCredito() != null && usuario.getCredito() >= alquileres.getPrecioAlquiler()) {


            // Descontar el crédito del usuario
            double credito = usuario.getCredito() - alquileres.getPrecioAlquiler();
            usuario.setCredito(credito);
            usuarioRepository.save(usuario);

            // Actualizar el estado del alquiler
            alquileres.setEstadoAlquiler(estadoAlquiler);
            alquileres.setFechaAlquiler(LocalDate.now());

            alquileresRepository.save(alquileres);

            //SIMULACION
            //deuda(idUsuario,idPelicula);
            // Enviar correo de confirmación
            emailService.alquilerCorreo(
                    usuario.getEmail(),
                    "Confirmación de alquiler de película",
                    "¡Hola " + usuario.getNombre() + "!\n\n" +
                            "Tu alquiler de la película '" + pelicula.getTitulo() + "' ha sido procesado con éxito.\n" +
                            "Precio del alquiler: " + alquileres.getPrecioAlquiler() +"€"+ "\n\n" + // Y el precio
                            "Puedes venir a recogerla en cualquier momento.\n\n" +
                            "Tienes 15 dias desde el dia del alquiler para devolver la pelicula sin tener recargos.\n\n" +
                            "Gracias por usar nuestro servicio."
            );


            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("correcto", "Alquiler realizado con éxito y correo enviado.");
        } else {
            // Si no tiene suficiente saldo
            redirectAttributes.addFlashAttribute("error", "Saldo insuficiente.");
        }
    }


    @Scheduled(cron = "0 14 12 * * ?")  // Se lanza a las  12:14min
    public void verificarDeudas() {
        System.out.println("-----> cron a las 12 ejecutando");
        List<Alquileres> alquileres = alquileresRepository.findAll();
        for (Alquileres alquiler : alquileres) {
            deuda(alquiler.getUsuario().getId_Usuario(), alquiler.getPelicula().getId_Pelicula());
        }
    }


    //Comprobacion local
    public void deuda(Integer idUsuario , Integer idPelicula) {


        Optional<Alquileres> optional =
                alquileresRepository.findByUsuario_Id_UsuarioAndPelicula_Id(idUsuario, idPelicula);

        if (optional.isPresent()) {

            Alquileres alquiler = optional.get();
            LocalDate fechaMax = alquiler.getFechaAlquiler().plusDays(15);
            LocalDate fechaAviso = fechaMax.minusDays(1);

            Usuario usuario1 = usuarioRepository.findById(idUsuario).orElse(null);
            Pelicula pelicula = peliculaRepository.findById(idPelicula).orElse(null);


            //Email comprobacion 1 di anterior aviso de devolver pelicula
            //Se tiene que poner 14 dias antes en base de datos para simular
            if (LocalDate.now().isAfter(fechaAviso.minusDays(1)) && LocalDate.now().isBefore(fechaAviso.plusDays(1)) && alquiler.getFechaDevolucion() == null) {

                System.out.println("---------------->Fecha actual: " + LocalDate.now());
                System.out.println("---------------->Fecha de aviso calculada: " + fechaAviso);
                System.out.println("---------------->Fecha de devolución: " + alquiler.getFechaDevolucion());
                System.out.println("Ejecutando  para usuario: " + idUsuario + " y película: " + idPelicula);

                emailService.recordatorioAlquiler(
                        usuario1.getEmail(),
                        "Recordatorio de devolución de tu película",
                        "Hola " + usuario1.getNombre() + ",\n\n" +
                                "Queremos recordarte que tu alquiler de \"" + pelicula.getTitulo() + "\" está por vencer.\n\n" +
                                "Fecha límite de devolución: " + fechaAviso.toString() + "\n\n" +
                                "Por favor, devuélvela antes de la fecha indicada para evitar recargos.\n\n" +
                                "Gracias por elegirnos.\n\n" +
                                "Si tienes alguna duda, estamos aquí para ayudarte."
                );

            }

            if(alquiler.getFechaDevolucion() != null && alquiler.getFechaDevolucion().isAfter(fechaMax)){

                Usuario usuario = alquiler.getUsuario();
                if(usuario.getDeuda()==null){
                    usuario.setDeuda(0);
                }

                usuario.setDeuda(usuario.getDeuda()+6);
                usuarioRepository.save(usuario);
            }

        }


    }


    public void devuelta(Integer idUsuario, Integer idPelicula , RedirectAttributes redirectAttributes ){

        Optional<Alquileres> alquileres =
                alquileresRepository.findByUsuarioIdAndPeliculaIdAndEstadoAlquiler(idUsuario, idPelicula, "alquilada");

        if(alquileres.isPresent() && alquileres !=null){
            Alquileres alquiler = alquileres.get();

            //si NO esta alquilada no puedes devolverla
            if("devuelta".equals(alquiler.getEstadoAlquiler())){
                alquiler.setEstadoAlquiler("alquilada");
                alquiler.setFechaDevolucion(LocalDate.now());

            }else{
                alquiler.setEstadoAlquiler("devuelta");
                alquiler.setFechaDevolucion(LocalDate.now());

            }


            Optional<EstadosPeliculas> estadoOptional= estadosPeliculasRepository.findByUsuario_Id_UsuarioAndPelicula_Id(idUsuario, idPelicula);



            if(estadoOptional.isPresent()){

            EstadosPeliculas estadoPelicula = estadoOptional.get();
            estadoPelicula.setFechaDevuelta(alquiler.getFechaDevolucion());
            estadosPeliculasRepository.save(estadoPelicula);
            }else{
                EstadosPeliculas nuevoEstado = new EstadosPeliculas();
                nuevoEstado.setUsuario(usuarioRepository.findById(idUsuario).orElse(null));
                nuevoEstado.setPelicula(peliculaRepository.findById(idPelicula).orElse(null));
                nuevoEstado.setDevuelta("true");
                nuevoEstado.setFechaDevuelta(alquiler.getFechaDevolucion());
                estadosPeliculasRepository.save(nuevoEstado);
            }

            alquileresRepository.save(alquiler);

            deuda(idUsuario, idPelicula);
            redirectAttributes.addFlashAttribute("correcto", "Película devuelta correctamente..");
        }else{
            redirectAttributes.addFlashAttribute("error", "No puedes devolver una película que no has alquilado.");
        }
    }



    public List<Alquileres> filtroAlquilada(Integer idUsuario){
        return alquileresRepository.filtroAlquilada(idUsuario);

    }

    public List<Alquileres> listaAlquileres(Integer idUsuario){
        return  alquileresRepository.filtroAlquilada(idUsuario);

    }

    public List<Alquileres> obtenerAlquileresActivos() {
        return alquileresRepository.findAlquileresActivos();
    }

    }








