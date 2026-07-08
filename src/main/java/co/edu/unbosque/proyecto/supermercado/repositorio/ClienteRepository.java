package co.edu.unbosque.proyecto.supermercado.repositorio;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Método derivado: Spring genera la query solo leyendo el nombre.
    // Útil para el login (buscar por nombre de usuario).
    Optional<Cliente> findByNombreUsuario(String nombreUsuario);

    // Útil para validar que la cédula no esté repetida antes de crear.
    boolean existsByCedula(String cedula);

    Optional<Cliente> findByCedula(String cedula);
}
