package com.proyectoTfg.demo.repository;

import com.proyectoTfg.demo.model.Alquileres;
import com.proyectoTfg.demo.model.Compra;
import jakarta.transaction.Transactional;
import org.hibernate.query.results.complete.CompleteFetchBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Integer> {


    //Pelicula comprada por el usuario y la pelicula
    @Query(value = "SELECT * FROM compras WHERE id_usuario = :idUsuario AND id_pelicula = :idPelicula", nativeQuery = true)
    Optional<Compra> UsuarioYPelicula(
            @Param("idUsuario") Integer idUsuario,
            @Param("idPelicula") Integer idPelicula);


    //Filtro comprada
    //Filtro peliculas alquilada
    @Query(value = "SELECT * FROM compras WHERE id_usuario =:id_usuario AND estado_Compra= 'comprado'", nativeQuery = true)
    List<Compra> filtroComprada(@Param("id_usuario") Integer id_usuario);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM alquileres WHERE id_usuario = :idUsuario", nativeQuery = true)
    void deleteByUsuarioId(@Param("idUsuario") Integer idUsuario);


}
