package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.SolicitudSobrecupo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitudSobrecupoRepository {

    private final JdbcTemplate jdbcTemplate;

    public SolicitudSobrecupoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String COLUMNAS =
            "cod_solicitud, fecha, hora, monto_solicitado, monto_autorizado, estado, "
                    + "id_usuario_cliente, id_usuario_pareja, id_usuario_supervisor";

    private final RowMapper<SolicitudSobrecupo> mapper = (rs, rowNum) -> {
        SolicitudSobrecupo s = new SolicitudSobrecupo();
        s.setCodSolicitud(rs.getLong("cod_solicitud"));
        s.setFecha(rs.getObject("fecha", LocalDate.class));
        s.setHora(rs.getObject("hora", LocalTime.class));
        s.setMontoSolicitado(rs.getBigDecimal("monto_solicitado"));
        s.setMontoAutorizado(rs.getBigDecimal("monto_autorizado")); // null si aun no fue aprobada

        s.setEstado(rs.getString("estado"));

        s.setIdUsuarioCliente(rs.getLong("id_usuario_cliente"));
        s.setIdUsuarioPareja(rs.getLong("id_usuario_pareja"));

        long idSupervisor = rs.getLong("id_usuario_supervisor");
        s.setIdUsuarioSupervisor(rs.wasNull() ? null : idSupervisor);

        return s;
    };

    public List<SolicitudSobrecupo> findAll() {
        String sql = "SELECT " + COLUMNAS + " FROM solicitud_sobrecupo ORDER BY fecha DESC, hora DESC";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<SolicitudSobrecupo> findById(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM solicitud_sobrecupo WHERE cod_solicitud = ?";
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    public List<SolicitudSobrecupo> findByIdUsuarioCliente(Long idCliente) {
        String sql = "SELECT " + COLUMNAS + " FROM solicitud_sobrecupo "
                + "WHERE id_usuario_cliente = ? ORDER BY fecha DESC, hora DESC";
        return jdbcTemplate.query(sql, mapper, idCliente);
    }

    public List<SolicitudSobrecupo> findByIdUsuarioPareja(Long idPareja) {
        String sql = "SELECT " + COLUMNAS + " FROM solicitud_sobrecupo "
                + "WHERE id_usuario_pareja = ? ORDER BY fecha DESC, hora DESC";
        return jdbcTemplate.query(sql, mapper, idPareja);
    }

    // Solicitudes que el cliente aun debe revisar
    public List<SolicitudSobrecupo> findPendientesByCliente(Long idCliente) {
        String sql = "SELECT " + COLUMNAS + " FROM solicitud_sobrecupo "
                + "WHERE id_usuario_cliente = ? AND estado = 'pendiente_cliente' ORDER BY fecha, hora";
        return jdbcTemplate.query(sql, mapper, idCliente);
    }

    // Solicitudes que el supervisor debe revisar
    public List<SolicitudSobrecupo> findPendientesBySupervisor(Long idSupervisor) {
        String sql = "SELECT " + COLUMNAS + " FROM solicitud_sobrecupo "
                + "WHERE id_usuario_supervisor = ? AND estado = 'pendiente_supervisor' ORDER BY fecha, hora";
        return jdbcTemplate.query(sql, mapper, idSupervisor);
    }

    // cod_solicitud es BIGSERIAL: se genera automáticamente en la BD
    public SolicitudSobrecupo save(SolicitudSobrecupo solicitud) {
        String sql = "INSERT INTO solicitud_sobrecupo "
                + "(fecha, hora, monto_solicitado, monto_autorizado, estado, "
                + "id_usuario_cliente, id_usuario_pareja, id_usuario_supervisor) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING cod_solicitud";

        Long codGenerado = jdbcTemplate.queryForObject(sql, Long.class,
                solicitud.getFecha(), solicitud.getHora(),
                solicitud.getMontoSolicitado(), solicitud.getMontoAutorizado(),
                solicitud.getEstado(),
                solicitud.getIdUsuarioCliente(), solicitud.getIdUsuarioPareja(),
                solicitud.getIdUsuarioSupervisor());

        solicitud.setCodSolicitud(codGenerado);
        return solicitud;
    }

    // Actualiza los campos que cambian durante el flujo de aprobacion/rechazo
    public SolicitudSobrecupo update(SolicitudSobrecupo solicitud) {
        String sql = "UPDATE solicitud_sobrecupo SET estado = ?, monto_autorizado = ?, "
                + "id_usuario_supervisor = ? WHERE cod_solicitud = ?";

        jdbcTemplate.update(sql,
                solicitud.getEstado(), solicitud.getMontoAutorizado(),
                solicitud.getIdUsuarioSupervisor(),
                solicitud.getCodSolicitud());

        return solicitud;
    }
}
