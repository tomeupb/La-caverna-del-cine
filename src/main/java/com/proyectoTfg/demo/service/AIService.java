package com.proyectoTfg.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    @Value("${openrouter.api.key}")
    private String apiKey;

    public String obtenerRespuesta(String mensaje) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate(); //para llamar a la api de openrouter
        HttpHeaders headers = new HttpHeaders(); //definir estructura a la peticion
        headers.set("Authorization", "Bearer " + apiKey); //autentificacion como por postman
        headers.setContentType(MediaType.APPLICATION_JSON); //autentificacion como por postman

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "Experto en el mundo del cine y en recomnedar peliculas"), // comportamiento
                        Map.of("role", "user", "content", mensaje) //contexto predefinido
                ),
                "max_tokens", 101 //tamaño respuesta
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers); //peticion


            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class); //guardamos la peticion en el jsonReponse
            String jsonResponse = response.getBody();
        System.out.println("Respuesta API cruda: " + jsonResponse);

            // Transformamos con objectMaper a jsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            String mensajeIA = rootNode.path("choices").get(0).path("message").path("content").asText();

            return mensajeIA;


        }
    }

