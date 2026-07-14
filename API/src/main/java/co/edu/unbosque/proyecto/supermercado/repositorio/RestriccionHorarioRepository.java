package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.RestriccionHorario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RestriccionHorarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public RestriccionHorarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String COLUMNAS =
            "id_restriccion, motivo, dia_bloqueo, hora_bloqueo_inicio, hora_bloqueo_fin, id_usuario_pareja";

    private final RowMapper<RestriccionHorario> mapper = (rs, rowNum) -> {
        RestriccionHorario r = new RestriccionHorario();
        r.setIdRestriccion(rs.getLong("id_restriccion"));
        r.setMotivo(rs.getString("motivo"));
        r.setDiaBloqueo(rs.getObject("dia_bloqueo", LocalDate.class));
        r.setHoraBloqueoInicio(rs.getObject("hora_bloqueo_inicio", LocalTime.class));
        r.setHoraBloqueoFin(rs.getObject("hora_bloqueo_fin", LocalTime.class));
        r.setIdUsuarioPareja(rs.getLong("id_usuario_pareja"));
        return r;
    };

    public List<RestriccionHorario> findAll() {
        String sql = "SELECT " + COLUMNAS + " FROM restriccion_horario ORDER BY id_restriccion";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<RestriccionHorario> findById(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM restriccion_horario WHERE id_restriccion = ?";
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    public List<RestriccionHorario> findByIdUsuarioPareja(Long idUsuarioPareja) {
        String sql = "SELECT " + COLUMNAS + " FROM restriccion_horario WHERE id_usuario_pareja = ? "
                + "ORDER BY dia_bloqueo, hora_bloqueo_inicio";
        return jdbcTemplate.query(sql, mapper, idUsuarioPareja);
    }

    public boolean existeBloqueoActivo(Long idUsuarioPareja, LocalDate diaCompra, LocalTime horaCompra) {
        String sql = "SELECT COUNT(*) FROM restriccion_horario "
                + "WHERE id_usuario_pareja = ? AND dia_bloqueo = ? "
                + "AND ? BETWEEN hora_bloqueo_inicio AND hora_bloqueo_fin";

        Integer total = jdbcTemplate.queryForObject(sql, Integer.class,
                idUsuarioPareja, diaCompra, horaCompra);
        return total != null && total > 0;
    }

    public RestriccionHorario save(RestriccionHorario restriccion) {
        String sql = "INSERT INTO restriccion_horario (motivo, dia_bloqueo, hora_bloqueo_inicio, "
                + "hora_bloqueo_fin, id_usuario_pareja) VALUES (?, ?, ?, ?, ?) RETURNING id_restriccion";

        Long idGenerado = jdbcTemplate.queryForObject(sql, Long.class,
                restriccion.getMotivo(), restriccion.getDiaBloqueo(),
                restriccion.getHoraBloqueoInicio(), restriccion.getHoraBloqueoFin(),
                restriccion.getIdUsuarioPareja());

        restriccion.setIdRestriccion(idGenerado);
        return restriccion;
    }

    public RestriccionHorario update(RestriccionHorario restriccion) {
        String sql = "UPDATE restriccion_horario SET motivo = ?, dia_bloqueo = ?, "
                + "hora_bloqueo_inicio = ?, hora_bloqueo_fin = ?, id_usuario_pareja = ? "
                + "WHERE id_restriccion = ?";

        jdbcTemplate.update(sql,
                restriccion.getMotivo(), restriccion.getDiaBloqueo(),
                restriccion.getHoraBloqueoInicio(), restriccion.getHoraBloqueoFin(),
                restriccion.getIdUsuarioPareja(), restriccion.getIdRestriccion());

        return restriccion;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM restriccion_horario WHERE id_restriccion = ?", id);
    }
}
