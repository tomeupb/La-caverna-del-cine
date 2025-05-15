
package com.proyectoTfg.demo.controller;

import com.proyectoTfg.demo.DTO.TarjetaDTO;
import com.proyectoTfg.demo.model.Usuario;


import com.proyectoTfg.demo.service.TransaccionService;
import jakarta.servlet.http.HttpSession;

import org.hibernate.annotations.processing.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Validated
@Controller
@RequestMapping("/pago")
public class TransaccionController {


    @Autowired
    TransaccionService transaccionService;


    @GetMapping("/transaccion")
    public String transaccion(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        return "transaccion";
    }


    @PostMapping("/ingresar")
    public String ingresar(@RequestParam Double ingreso, HttpSession session, RedirectAttributes redirectAttributes) {

        Boolean tarjetaValidada = (Boolean) session.getAttribute("tarjetaValidada");

        if (tarjetaValidada == null || !tarjetaValidada) {
            redirectAttributes.addFlashAttribute("error", "Debes validar la tarjeta antes de realizar el ingreso.");
            return "redirect:/pago/transaccion";
        }
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            Integer idUsuario = usuario.getId_Usuario();

            transaccionService.ingreso(idUsuario, ingreso);

            usuario.setCredito(usuario.getCredito() + ingreso); // ver actualizacion por pantalla
            session.setAttribute("usuario", usuario);  // Guardar para ver actualizacion por pantalla

            redirectAttributes.addFlashAttribute("correcto", "El ingreso  realizado con éxito");
            return "redirect:/pago/transaccion";
        } else {
            redirectAttributes.addFlashAttribute("error", "Session perdida");
            return "redirect:/pago/transaccion";
        }
    }


    @PostMapping("/deuda")
    public String deuda(@RequestParam Double pagoDeuda, HttpSession session, RedirectAttributes redirectAttributes) {

        Boolean tarjetaValidada = (Boolean) session.getAttribute("tarjetaValidada");

        if (tarjetaValidada == null || !tarjetaValidada) {
            redirectAttributes.addFlashAttribute("error", "Debes validar la tarjeta antes pagar comisiones.");
            return "redirect:/pago/transaccion";
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");


        if (usuario.getDeuda() >= pagoDeuda && pagoDeuda<= usuario.getCredito()) {
            Integer idUsuario = usuario.getId_Usuario();

            transaccionService.deuda(idUsuario, pagoDeuda, redirectAttributes);
            usuario.setDeuda((int) ((double) usuario.getDeuda() - pagoDeuda));  //Para ver el cambio por pantalla
            usuario.setCredito(usuario.getCredito() - pagoDeuda);  // Para ver el cambio por pantalla
            session.setAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("correcto", "La deuda ha sido pagada con éxito");
            return "redirect:/pago/transaccion";

        }else if(pagoDeuda>= usuario.getCredito()){
            redirectAttributes.addFlashAttribute("error", "No tienes el saldo necesario para realizar la operacion");
            return "redirect:/pago/transaccion";
        } else {

            redirectAttributes.addFlashAttribute("error", "No puedes pagar mas de la deuda que tienes");
            return "redirect:/pago/transaccion";
        }

    }

    @PostMapping("/validar")
    public String validar(@Validated TarjetaDTO tarjetaDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        boolean tipoValido = tarjetaDTO.getTipoTarjeta().equalsIgnoreCase("visa") ||
                tarjetaDTO.getTipoTarjeta().equalsIgnoreCase("mastercard") ||
                tarjetaDTO.getTipoTarjeta().equalsIgnoreCase("amex");

        if (tipoValido) {
            session.setAttribute("tarjetaValidada", true); // Guardar estado en sesión
            redirectAttributes.addFlashAttribute("correcto", "Tarjeta validada correctamente.");
        } else {
            session.setAttribute("tarjetaValidada", false);
            redirectAttributes.addFlashAttribute("error", "Tarjeta rechazada.");
        }

        return "redirect:/pago/transaccion";
    }





    }



