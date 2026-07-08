package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.time.LocalTime;
import java.util.List;

import co.edu.unbosque.proyecto.supermercado.modelo.RestriccionHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestriccionHorarioRepository extends JpaRepository<RestriccionHorario, Long> {

    // Todas las restricciones configuradas para una pareja (relación Tener)
    List<RestriccionHorario> findByPareja_IdUsuario(Long idUsuarioPareja);

    // Verifica si existe una restricción activa para la pareja en el día
    // y la hora en que se intenta hacer una compra. Se usa para bloquear
    // la compra en el punto de venta, según el enunciado.
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM RestriccionHorario r " +
            "WHERE r.pareja.idUsuario = :idUsuarioPareja " +
            "AND r.diaBloqueo = :diaBloqueo " +
            "AND :horaCompra BETWEEN r.horaBloqueoInicio AND r.horaBloqueoFin")
    boolean existeBloqueoActivo(@Param("idUsuarioPareja") Long idUsuarioPareja,
                                @Param("diaBloqueo") String diaBloqueo,
                                @Param("horaCompra") LocalTime horaCompra);
}
