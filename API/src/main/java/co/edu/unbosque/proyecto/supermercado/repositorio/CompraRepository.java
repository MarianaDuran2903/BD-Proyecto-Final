package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.Compra;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CompraRepository {

    private final JdbcTemplate jdbcTemplate;

    public CompraRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String COLUMNAS =
            "cod_compra, monto, fecha, hora, id_usuario_pareja, id_usuario_cliente, "
                    + "id_almacen, id_usuario_supervisor";

    private final RowMapper<Compra> mapper = (rs, rowNum) -> {
        Compra c = new Compra();
        c.setCodCompra(rs.getLong("cod_compra"));
        c.setMonto(rs.getBigDecimal("monto"));
        c.setFecha(rs.getObject("fecha", LocalDate.class));
        c.setHora(rs.getObject("hora", LocalTime.class));

        long idPareja = rs.getLong("id_usuario_pareja");
        c.setIdUsuarioPareja(rs.wasNull() ? null : idPareja);

        long idCliente = rs.getLong("id_usuario_cliente");
        c.setIdUsuarioCliente(rs.wasNull() ? null : idCliente);

        c.setIdAlmacen(rs.getLong("id_almacen"));
        c.setIdUsuarioSupervisor(rs.getLong("id_usuario_supervisor"));

        return c;
    };

    public List<Compra> findAll() {
        String sql = "SELECT " + COLUMNAS + " FROM compra ORDER BY fecha DESC, hora DESC";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Compra> findById(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM compra WHERE cod_compra = ?";
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    public List<Compra> findByIdUsuarioPareja(Long idUsuarioPareja) {
        String sql = "SELECT " + COLUMNAS + " FROM compra WHERE id_usuario_pareja = ? ORDER BY fecha DESC, hora DESC";
        return jdbcTemplate.query(sql, mapper, idUsuarioPareja);
    }

    // Compras directas del cliente + compras de todas sus parejas (JOIN con pareja)
    public List<Compra> findByIdUsuarioCliente(Long idUsuarioCliente) {
        String sql = "SELECT cod_compra, monto, fecha, hora, id_usuario_pareja, id_usuario_cliente, "
                + "id_almacen, id_usuario_supervisor FROM compra WHERE id_usuario_cliente = ? "
                + "UNION "
                + "SELECT c.cod_compra, c.monto, c.fecha, c.hora, c.id_usuario_pareja, c.id_usuario_cliente, "
                + "c.id_almacen, c.id_usuario_supervisor "
                + "FROM compra c JOIN pareja p ON c.id_usuario_pareja = p.id_usuario "
                + "WHERE p.id_usuario_cliente = ? "
                + "ORDER BY fecha DESC, hora DESC";

        return jdbcTemplate.query(sql, mapper, idUsuarioCliente, idUsuarioCliente);
    }

    // cod_compra es BIGSERIAL: se genera automáticamente en la BD
    public Compra save(Compra compra) {
        String sql = "INSERT INTO compra (monto, fecha, hora, id_usuario_pareja, id_usuario_cliente, "
                + "id_almacen, id_usuario_supervisor) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING cod_compra";

        Long codGenerado = jdbcTemplate.queryForObject(sql, Long.class,
                compra.getMonto(), compra.getFecha(), compra.getHora(),
                compra.getIdUsuarioPareja(), compra.getIdUsuarioCliente(),
                compra.getIdAlmacen(), compra.getIdUsuarioSupervisor());

        compra.setCodCompra(codGenerado);
        return compra;
    }

    public void actualizarSupervisor(Long codCompra, Long idUsuarioSupervisor) {
        String sql = "UPDATE compra SET id_usuario_supervisor = ? WHERE cod_compra = ?";
        jdbcTemplate.update(sql, idUsuarioSupervisor, codCompra);
    }
}
