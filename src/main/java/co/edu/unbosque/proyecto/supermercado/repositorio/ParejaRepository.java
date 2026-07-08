package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.Pareja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ParejaRepository extends JpaRepository<Pareja, Long> {

    Optional<Pareja> findByNombreUsuario(String nombreUsuario);

    boolean existsByCedula(String cedula);

    // Todas las parejas de un cliente titular (relación Poseer)
    List<Pareja> findByCliente_IdUsuario(Long idUsuarioCliente);

    // Ejemplo de JPQL con @Query: sigue siendo ORM (trabaja sobre la
    // entidad Pareja y sus atributos Java, no sobre nombres de tabla SQL).
    // Esta query apoya la regla de negocio "Límite de Integridad":
    // suma de cupos asignados a las parejas de un cliente.
    @Query("SELECT COALESCE(SUM(p.cupoAsignado), 0) FROM Pareja p WHERE p.cliente.idUsuario = :idUsuarioCliente")
    BigDecimal sumarCupoAsignadoPorCliente(@Param("idUsuarioCliente") Long idUsuarioCliente);
}