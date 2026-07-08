package co.edu.unbosque.proyecto.supermercado.repositorio;


import java.time.LocalDate;
import java.util.List;
import co.edu.unbosque.proyecto.supermercado.modelo.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    // Historial de compras de una pareja (relación Realizar)
    List<Compra> findByPareja_IdUsuario(Long idUsuarioPareja);

    // Compras efectuadas en un almacén específico (relación Efectuar)
    List<Compra> findByAlmacen_IdAlmacen(Long idAlmacen);

    // Compras de una pareja en una fecha específica, útil para el
    // reporte de "detalle minucioso de compras" pedido en el enunciado.
    List<Compra> findByPareja_IdUsuarioAndFecha(Long idUsuarioPareja, LocalDate fecha);

    // Compras que quedaron marcadas con requiere_sobrecupo = true
    List<Compra> findByRequiereSobrecupoTrue();

    // Suma de lo ya consumido del cupo por una pareja, usada para
    // calcular el cupo disponible antes de registrar una nueva compra.
    @Query("SELECT COALESCE(SUM(c.monto), 0) FROM Compra c WHERE c.pareja.idUsuario = :idUsuarioPareja")
    BigDecimal sumarMontoPorPareja(@Param("idUsuarioPareja") Long idUsuarioPareja);
}