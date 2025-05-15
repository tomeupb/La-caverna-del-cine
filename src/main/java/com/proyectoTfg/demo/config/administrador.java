package com.proyectoTfg.demo.config;
import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.repository.UsuarioRepository;
import com.proyectoTfg.demo.util.HashPassword;
import com.stripe.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class administrador implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByEmail("admin@gmail.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setCredito(10000.0);
            admin.setDeuda(0);
            admin.setEmail("admin@gmail.com");
            admin.setPassword(HashPassword.hashPassword("admin"));
            admin.setRol("ADMIN");
            admin.setSexo("Hombre");
            usuarioRepository.save(admin);
        }
    }


}
