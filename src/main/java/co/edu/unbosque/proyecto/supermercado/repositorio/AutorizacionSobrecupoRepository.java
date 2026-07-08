package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.AutorizacionSobrecupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorizacionSobrecupoRepository extends JpaRepository<AutorizacionSobrecupo, Long> {

    // Relación Tener (1,1)-(0,1): a lo sumo una autorización por compra
    Optional<AutorizacionSobrecupo> findByCompra_IdCompra(Long idCompra);

    // Todas las autorizaciones aprobadas por un supervisor (relación Aprobar)
    List<AutorizacionSobrecupo> findBySupervisor_IdUsuario(Long idUsuarioSupervisor);

    // Todas las autorizaciones concedidas por un cliente titular (relación Autorizar)
    List<AutorizacionSobrecupo> findByCliente_IdUsuario(Long idUsuarioCliente);
}
