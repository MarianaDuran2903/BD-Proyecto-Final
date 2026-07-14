package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.Pareja;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ParejaRepository {

    private final JdbcTemplate jdbcTemplate;

    public ParejaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String COLUMNAS =
            "id_usuario, nombre_usuario, contrasenia, estado, primer_nombre, segundo_nombre, "
                    + "primer_apellido, segundo_apellido, telefono, cupo_asignado, id_usuario_cliente";

    private final RowMapper<Pareja> mapper = (rs, rowNum) -> {
        Pareja p = new Pareja();
        p.setIdUsuario(rs.getLong("id_usuario"));
        p.setNombreUsuario(rs.getString("nombre_usuario"));
        p.setContrasenia(rs.getString("contrasenia"));
        p.setEstado(rs.getString("estado"));
        p.setPrimerNombre(rs.getString("primer_nombre"));
        p.setSegundoNombre(rs.getString("segundo_nombre"));
        p.setPrimerApellido(rs.getString("primer_apellido"));
        p.setSegundoApellido(rs.getString("segundo_apellido"));
        p.setTelefono(rs.getString("telefono"));
        p.setCupoAsignado(rs.getBigDecimal("cupo_asignado"));
        p.setIdUsuarioCliente(rs.getLong("id_usuario_cliente"));
        return p;
    };

    public List<Pareja> findAll() {
        String sql = "SELECT " + COLUMNAS + " FROM pareja ORDER BY id_usuario";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Pareja> findById(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM pareja WHERE id_usuario = ?";
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    public Optional<Pareja> findByNombreUsuario(String nombreUsuario) {
        String sql = "SELECT " + COLUMNAS + " FROM pareja WHERE nombre_usuario = ?";
        return jdbcTemplate.query(sql, mapper, nombreUsuario).stream().findFirst();
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM pareja WHERE id_usuario = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return total != null && total > 0;
    }

    public List<Pareja> findByIdUsuarioCliente(Long idUsuarioCliente) {
        String sql = "SELECT " + COLUMNAS + " FROM pareja WHERE id_usuario_cliente = ? ORDER BY id_usuario";
        return jdbcTemplate.query(sql, mapper, idUsuarioCliente);
    }

    public BigDecimal calcularSaldoDisponible(Long idPareja) {
        String sql = "SELECT p.cupo_asignado - COALESCE(SUM(c.monto), 0) "
                + "FROM pareja p LEFT JOIN compra c ON c.id_usuario_pareja = p.id_usuario "
                + "WHERE p.id_usuario = ? GROUP BY p.cupo_asignado";
        BigDecimal saldo = jdbcTemplate.queryForObject(sql, BigDecimal.class, idPareja);
        return saldo != null ? saldo : BigDecimal.ZERO;
    }

    public BigDecimal sumarCupoAsignadoPorCliente(Long idUsuarioCliente) {
        String sql = "SELECT COALESCE(SUM(cupo_asignado), 0) FROM pareja WHERE id_usuario_cliente = ?";
        BigDecimal suma = jdbcTemplate.queryForObject(sql, BigDecimal.class, idUsuarioCliente);
        return suma != null ? suma : BigDecimal.ZERO;
    }

    // Igual que sumarCupoAsignadoPorCliente, pero sin contar una pareja puntual.
    // Se usa al actualizar una pareja ya existente: su propio cupo_asignado (todavia
    // no persistido con el valor nuevo) no debe contarse dos veces en la validacion.
    public BigDecimal sumarCupoAsignadoPorClienteExcluyendo(Long idUsuarioCliente, Long idParejaExcluida) {
        String sql = "SELECT COALESCE(SUM(cupo_asignado), 0) FROM pareja "
                + "WHERE id_usuario_cliente = ? AND id_usuario <> ?";
        BigDecimal suma = jdbcTemplate.queryForObject(sql, BigDecimal.class, idUsuarioCliente, idParejaExcluida);
        return suma != null ? suma : BigDecimal.ZERO;
    }

    public Pareja save(Pareja pareja) {
        String sql = "INSERT INTO pareja (id_usuario, nombre_usuario, contrasenia, estado, "
                + "primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, "
                + "telefono, cupo_asignado, id_usuario_cliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                pareja.getIdUsuario(), pareja.getNombreUsuario(), pareja.getContrasenia(),
                pareja.getEstado(), pareja.getPrimerNombre(), pareja.getSegundoNombre(),
                pareja.getPrimerApellido(), pareja.getSegundoApellido(),
                pareja.getTelefono(), pareja.getCupoAsignado(), pareja.getIdUsuarioCliente());

        return pareja;
    }

    public Pareja update(Pareja pareja) {
        String sql = "UPDATE pareja SET nombre_usuario = ?, contrasenia = ?, estado = ?, "
                + "primer_nombre = ?, segundo_nombre = ?, primer_apellido = ?, segundo_apellido = ?, "
                + "telefono = ?, cupo_asignado = ?, id_usuario_cliente = ? WHERE id_usuario = ?";

        jdbcTemplate.update(sql,
                pareja.getNombreUsuario(), pareja.getContrasenia(), pareja.getEstado(),
                pareja.getPrimerNombre(), pareja.getSegundoNombre(),
                pareja.getPrimerApellido(), pareja.getSegundoApellido(),
                pareja.getTelefono(), pareja.getCupoAsignado(),
                pareja.getIdUsuarioCliente(), pareja.getIdUsuario());

        return pareja;
    }

    public void actualizarCupoAsignado(Long idPareja, BigDecimal nuevoCupoAsignado) {
        String sql = "UPDATE pareja SET cupo_asignado = ? WHERE id_usuario = ?";
        jdbcTemplate.update(sql, nuevoCupoAsignado, idPareja);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM pareja WHERE id_usuario = ?", id);
    }
}
