package co.edu.unbosque.proyecto.supermercado.repositorio;


import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

    Optional<Supervisor> findByNombreUsuario(String nombreUsuario);

    boolean existsByCedula(String cedula);

    // Todos los supervisores que trabajan en un almacén (relación Trabajar)
    List<Supervisor> findByAlmacen_IdAlmacen(Long idAlmacen);
}