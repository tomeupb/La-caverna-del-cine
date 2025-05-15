package com.proyectoTfg.demo.repository;

import com.proyectoTfg.demo.model.Transaccion;
import com.proyectoTfg.demo.model.UsuariosPeliculas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {

    @Query("SELECT t FROM Transaccion t WHERE t.usuario.id_Usuario = :idUsuario ")
    Transaccion findByUsuarioIdAndPeliculaId(@Param("idUsuario") Integer idUsuario);



    @Query(value = "SELECT SUM(t.ingreso) "+
            "FROM transacciones t "+
            "WHERE t.id_Usuario = :id_usuario", nativeQuery = true)
    Integer sumaTotalIngresos(@Param("id_usuario")Long idUsuario);



}
