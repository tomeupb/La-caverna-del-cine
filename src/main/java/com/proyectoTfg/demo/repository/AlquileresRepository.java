package com.proyectoTfg.demo.repository;

import com.proyectoTfg.demo.model.Alquileres;
import com.proyectoTfg.demo.model.EstadosPeliculas;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlquileresRepository extends JpaRepository<Alquileres, Integer> {

    @Query(value = "SELECT * FROM alquileres WHERE id_usuario = :idUsuario AND id_pelicula = :idPelicula", nativeQuery = true)
    Optional<Alquileres> findByUsuario_Id_UsuarioAndPelicula_Id(
            @Param("idUsuario") Integer idUsuario,
            @Param("idPelicula") Integer idPelicula);


    @Query(value = "SELECT * FROM alquileres WHERE id_usuario = :idUsuario AND id_pelicula = :idPelicula AND estado_alquiler = :estadoAlquiler", nativeQuery = true)
    Optional<Alquileres> findByUsuarioIdAndPeliculaIdAndEstadoAlquiler(
            @Param("idUsuario") Integer idUsuario,
            @Param("idPelicula") Integer idPelicula,
            @Param("estadoAlquiler") String estadoAlquiler);

    //Filtro peliculas alquilada
    @Query(value ="SELECT * FROM alquileres WHERE id_usuario =:id_usuario AND estado_alquiler= 'alquilada'" ,nativeQuery = true)
    List<Alquileres> filtroAlquilada(@Param("id_usuario")Integer id_usuario);


    @Query("SELECT a FROM Alquileres a WHERE a.estadoAlquiler = 'alquilada'")
    List<Alquileres> findAlquileresActivos();


}


