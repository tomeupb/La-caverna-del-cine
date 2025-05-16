package com.proyectoTfg.demo.service;


import com.proyectoTfg.demo.model.Usuario;
import com.proyectoTfg.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {


    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EstadosPeliculasRepository estadosPeliculasRepository;


    @Autowired
    UsuariosPeliculasRepository usuariosPeliculasRepository;

    @Autowired
    TransaccionRepository transaccionRepository;
    @Autowired
    CompraRepository compraRepository;

    @Autowired
    AlquileresRepository alquileresRepository;
    //crear
    public Usuario addUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    //verificador email único

    public boolean uniqueEmail(String email){

        Usuario unico = usuarioRepository.findUsuarioByEmail(email);

        return unico ==null;

    }

    //borrar
    @Transactional
    public void adminDelete(Integer id_Usuario){
        compraRepository.deleteByUsuarioId(id_Usuario);
        usuariosPeliculasRepository.deleteByUsuarioId(id_Usuario);
        estadosPeliculasRepository.deleteByUsuarioId(id_Usuario);
        transaccionRepository.deleteByUsuarioId(id_Usuario);
        usuarioRepository.deleteById(id_Usuario);

    }

    //buscar por id
    public Usuario findById(Integer id_Usuario){

      Optional<Usuario> usuario = usuarioRepository.findById(id_Usuario);

       if(usuario.isPresent()){
           return usuario.get();
       }else{
           return null;
       }

    }



    //update
    public Usuario adminUpdate(Usuario usuario){
        return usuarioRepository.save(usuario);

    }
    //listar
    public List<Usuario> adminGetAll(){
       List<Usuario>usuarios = usuarioRepository.findAll();
        if(!usuarios.isEmpty()){
            return usuarios;
        }else{
            return null;
        }

    }

    //buscar por email
    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findUsuarioByEmail(email);
    }





}
