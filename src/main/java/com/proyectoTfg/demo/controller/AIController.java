package com.proyectoTfg.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.proyectoTfg.demo.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ia")
public class AIController {
//https://openrouter.ai/settings/keys
    @Autowired
    private AIService aiService;

    @GetMapping("/preguntar")
    public String preguntar(@RequestParam(required = false) String mensaje, Model model) throws JsonProcessingException {
        if (mensaje == null ){
            model.addAttribute("respuesta", "Por favor ingresa un mensaje válido.");
        } else {
            String respuesta = aiService.obtenerRespuesta(mensaje);
            model.addAttribute("respuesta", respuesta);
        }
        return "chat";
    }

    @GetMapping("/chat")
        public String chat(){
            return "chat";
        }
}


