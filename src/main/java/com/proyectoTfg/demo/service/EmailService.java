package com.proyectoTfg.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    //Correo de registro de una pelicula
    public void registerCorreo(String para, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("Lacavernadelcinemallorca@gmail.com");
        //mailSender.send(mensaje);
    }


    //Correo de alquiler de una Peliciula
    public void alquilerCorreo(String para, String asunto, String cuerpo){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("Lacavernadelcinemallorca@gmail.com");
       //mailSender.send(mensaje);
    }


    //Correo de Ingreso de dinero
    public void ingresoCorreo(String para, String asunto, String cuerpo){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("Lacavernadelcinemallorca@gmail.com");
       //mailSender.send(mensaje);
    }

    //Correo de compra de Pelicula
    public void compraCorreo(String para, String asunto, String cuerpo){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("Lacavernadelcinemallorca@gmail.com");
        //mailSender.send(mensaje);
    }


    //Recordatorio 1 dia anterior a vencer el alquiler
    public void recordatorioAlquiler(String para, String asunto, String cuerpo){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("Lacavernadelcinemallorca@gmail.com");
        //mailSender.send(mensaje);
    }



}
