package com.proyectoTfg.demo.repository;

import com.proyectoTfg.demo.model.Alquileres;
import com.proyectoTfg.demo.model.UsuariosPeliculas;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuariosPeliculasRepository  extends JpaRepository<UsuariosPeliculas, Integer> {

    @Query("SELECT up FROM UsuariosPeliculas up WHERE up.usuario.id_Usuario = :idUsuario AND up.pelicula.id_Pelicula = :idPelicula")
    UsuariosPeliculas findByUsuarioIdAndPeliculaId(@Param("idUsuario") Integer idUsuario, @Param("idPelicula") Integer idPelicula);


    //Filtro peliculas favoritas
    @Query(value = "SELECT * FROM  usuarios_peliculas WHERE  id_usuario = :id_usuario AND favorita = 'true'" , nativeQuery=true)
    List<UsuariosPeliculas> filtroFavorito(@Param("id_usuario")Integer id_usuario);


    //filtro todas las peliculas
    @Query(value =  "SELECT * FROM usuarios_peliculas WHERE  id_usuario = :id_usuario " , nativeQuery = true)
    List<UsuariosPeliculas> obtenerTodas (@Param("id_usuario")Integer id_usuario);



    //filtramos segun calificacion
    @Query(value= "SELECT * FROM usuarios_peliculas WHERE id_usuario = :idUsuario ORDER BY calificacion ASC"
            ,nativeQuery = true)
    List<UsuariosPeliculas> listaCalificadaAsc (@Param("idUsuario")Integer idUsuario);





    //Filtros de Estadísticas

    //peliculas mas vistas por genero
    @Query(value = "SELECT COUNT(*) " +
            "FROM usuarios_peliculas up " +
            "JOIN peliculas p ON up.id_pelicula = p.id_pelicula " +
            "JOIN usuarios u ON up.id_usuario = u.id_usuario " +
            "WHERE p.genero = :genero AND u.sexo = :sexo", nativeQuery = true)
    Long peliculasVistasPorGenero(@Param("genero") String genero, @Param("sexo") String sexo);


    //Calificaciones por genero
    @Query(value = "SELECT SUM(up.calificacion) " +
            "FROM usuarios_peliculas up " +
            "JOIN peliculas p ON up.id_pelicula = p.id_pelicula " +
            "JOIN usuarios u ON up.id_usuario = u.id_usuario " +
            "WHERE p.genero = :genero", nativeQuery = true)
    Long calificacionesGenero(@Param("genero") String genero);



    //genero que mas compra peliculas

    @Query(value = "SELECT COUNT(*) " +
            "FROM compras c " +
            "JOIN usuarios u ON c.id_usuario = u.id_usuario " +
            "WHERE c.estado_compra = 'comprado' AND u.sexo = :sexo", nativeQuery = true)
    Long comprasPorSexo(@Param("sexo") String sexo);


    //Genero que mas alquila peliculas

    @Query(value = "SELECT COUNT(*) " +
            "FROM alquileres a " +
            "JOIN usuarios u ON a.id_usuario = u.id_usuario " +
            "WHERE a.estado_alquiler = 'alquilada' AND u.sexo = :sexo", nativeQuery = true)
    Long alquilerPorSexo(@Param("sexo") String sexo);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM alquileres WHERE id_usuario = :idUsuario", nativeQuery = true)
    void deleteByUsuarioId(@Param("idUsuario") Integer idUsuario);




}
