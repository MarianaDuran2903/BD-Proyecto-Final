package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.Cliente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // id_usuario ES la cedula del cliente (BIGINT proporcionado por el usuario)
    private static final String COLUMNAS =
            "id_usuario, nombre_usuario, contrasenia, estado, primer_nombre, "
                    + "segundo_nombre, primer_apellido, segundo_apellido, telefono, cupo_propio";

    private final RowMapper<Cliente> mapper = (rs, rowNum) -> {
        Cliente c = new Cliente();
        c.setIdUsuario(rs.getLong("id_usuario"));
        c.setNombreUsuario(rs.getString("nombre_usuario"));
        c.setContrasenia(rs.getString("contrasenia"));
        c.setEstado(rs.getString("estado"));
        c.setPrimerNombre(rs.getString("primer_nombre"));
        c.setSegundoNombre(rs.getString("segundo_nombre"));
        c.setPrimerApellido(rs.getString("primer_apellido"));
        c.setSegundoApellido(rs.getString("segundo_apellido"));
        c.setTelefono(rs.getString("telefono"));
        c.setCupoPropio(rs.getBigDecimal("cupo_propio"));
        return c;
    };

    public List<Cliente> findAll() {
        String sql = "SELECT " + COLUMNAS + " FROM cliente ORDER BY id_usuario";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Cliente> findById(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM cliente WHERE id_usuario = ?";
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    public Optional<Cliente> findByNombreUsuario(String nombreUsuario) {
        String sql = "SELECT " + COLUMNAS + " FROM cliente WHERE nombre_usuario = ?";
        return jdbcTemplate.query(sql, mapper, nombreUsuario).stream().findFirst();
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE id_usuario = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return total != null && total > 0;
    }

    // id_usuario (cedula) lo provee el cliente del API, no se auto-genera
    public Cliente save(Cliente cliente) {
        String sql = "INSERT INTO cliente (id_usuario, nombre_usuario, contrasenia, estado, "
                + "primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, "
                + "telefono, cupo_propio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                cliente.getIdUsuario(), cliente.getNombreUsuario(), cliente.getContrasenia(),
                cliente.getEstado(), cliente.getPrimerNombre(), cliente.getSegundoNombre(),
                cliente.getPrimerApellido(), cliente.getSegundoApellido(),
                cliente.getTelefono(), cliente.getCupoPropio());

        return cliente;
    }

    public Cliente update(Cliente cliente) {
        String sql = "UPDATE cliente SET nombre_usuario = ?, contrasenia = ?, "
                + "primer_nombre = ?, segundo_nombre = ?, primer_apellido = ?, segundo_apellido = ?, "
                + "telefono = ?, cupo_propio = ? WHERE id_usuario = ?";

        jdbcTemplate.update(sql,
                cliente.getNombreUsuario(), cliente.getContrasenia(),
                cliente.getPrimerNombre(), cliente.getSegundoNombre(),
                cliente.getPrimerApellido(), cliente.getSegundoApellido(),
                cliente.getTelefono(), cliente.getCupoPropio(),
                cliente.getIdUsuario());

        return cliente;
    }

    // Saldo disponible del cupo propio del cliente = cupo_propio - suma de sus compras directas
    public BigDecimal calcularSaldoPropioDisponible(Long idCliente) {
        String sql = "SELECT c.cupo_propio - COALESCE(SUM(co.monto), 0) "
                + "FROM cliente c LEFT JOIN compra co ON co.id_usuario_cliente = c.id_usuario "
                + "WHERE c.id_usuario = ? GROUP BY c.cupo_propio";
        BigDecimal saldo = jdbcTemplate.queryForObject(sql, BigDecimal.class, idCliente);
        return saldo != null ? saldo : BigDecimal.ZERO;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM cliente WHERE id_usuario = ?", id);
    }
}
