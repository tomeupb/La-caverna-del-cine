package com.proyectoTfg.demo.repository;

import com.proyectoTfg.demo.model.EstadosPeliculas;
import jakarta.transaction.Transactional;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstadosPeliculasRepository extends JpaRepository<EstadosPeliculas , Integer> {


    @Query(value = "SELECT * FROM estados_peliculas WHERE id_usuario = :idUsuario AND id_pelicula = :idPelicula", nativeQuery = true)
    Optional<EstadosPeliculas> findByUsuario_Id_UsuarioAndPelicula_Id(
            @Param("idUsuario") Integer idUsuario,
            @Param("idPelicula") Integer idPelicula);



    //Filtro peliculas vistas
    @Query(value ="SELECT * FROM estados_peliculas WHERE id_usuario =:id_usuario AND estado= 'vista'" ,nativeQuery = true)
    List<EstadosPeliculas> filtroVista(@Param("id_usuario")Integer id_usuario);

    //Filtro peliculas pendientes
    @Query(value ="SELECT * FROM estados_peliculas WHERE id_usuario =:id_usuario AND estado= 'pendiente'" ,nativeQuery = true)
    List<EstadosPeliculas> filtroPendiente(@Param("id_usuario")Integer id_usuario);

    //Filtro peliculas posesion
    @Query(value ="SELECT * FROM estados_peliculas WHERE id_usuario =:id_usuario AND estado= 'posesion'" ,nativeQuery = true)
    List<EstadosPeliculas> filtroPosesion(@Param("id_usuario")Integer id_usuario);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM alquileres WHERE id_usuario = :idUsuario", nativeQuery = true)
    void deleteByUsuarioId(@Param("idUsuario") Integer idUsuario);


}
