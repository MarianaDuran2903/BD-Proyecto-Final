package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.Supervisor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SupervisorRepository {

    private final JdbcTemplate jdbcTemplate;

    public SupervisorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // id_usuario ES la cedula del supervisor (BIGINT proporcionado por el usuario)
    private static final String COLUMNAS =
            "id_usuario, nombre_usuario, contrasenia, estado, correo, telefono, "
                    + "primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, id_almacen";

    private final RowMapper<Supervisor> mapper = (rs, rowNum) -> {
        Supervisor s = new Supervisor();
        s.setIdUsuario(rs.getLong("id_usuario"));
        s.setNombreUsuario(rs.getString("nombre_usuario"));
        s.setContrasenia(rs.getString("contrasenia"));
        s.setEstado(rs.getString("estado"));
        s.setCorreo(rs.getString("correo"));
        s.setTelefono(rs.getString("telefono"));
        s.setPrimerNombre(rs.getString("primer_nombre"));
        s.setSegundoNombre(rs.getString("segundo_nombre"));
        s.setPrimerApellido(rs.getString("primer_apellido"));
        s.setSegundoApellido(rs.getString("segundo_apellido"));
        s.setIdAlmacen(rs.getLong("id_almacen"));
        return s;
    };

    public List<Supervisor> findAll() {
        String sql = "SELECT " + COLUMNAS + " FROM supervisor ORDER BY id_usuario";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Supervisor> findById(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM supervisor WHERE id_usuario = ?";
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    public Optional<Supervisor> findByNombreUsuario(String nombreUsuario) {
        String sql = "SELECT " + COLUMNAS + " FROM supervisor WHERE nombre_usuario = ?";
        return jdbcTemplate.query(sql, mapper, nombreUsuario).stream().findFirst();
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM supervisor WHERE id_usuario = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return total != null && total > 0;
    }

    public List<Supervisor> findByIdAlmacen(Long idAlmacen) {
        String sql = "SELECT " + COLUMNAS + " FROM supervisor WHERE id_almacen = ? ORDER BY id_usuario";
        return jdbcTemplate.query(sql, mapper, idAlmacen);
    }

    // id_usuario (cedula) lo provee el cliente del API, no se auto-genera
    public Supervisor save(Supervisor supervisor) {
        String sql = "INSERT INTO supervisor (id_usuario, nombre_usuario, contrasenia, estado, "
                + "correo, telefono, primer_nombre, segundo_nombre, "
                + "primer_apellido, segundo_apellido, id_almacen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                supervisor.getIdUsuario(), supervisor.getNombreUsuario(), supervisor.getContrasenia(),
                supervisor.getEstado(), supervisor.getCorreo(), supervisor.getTelefono(),
                supervisor.getPrimerNombre(), supervisor.getSegundoNombre(),
                supervisor.getPrimerApellido(), supervisor.getSegundoApellido(),
                supervisor.getIdAlmacen());

        return supervisor;
    }

    public Supervisor update(Supervisor supervisor) {
        String sql = "UPDATE supervisor SET nombre_usuario = ?, contrasenia = ?, "
                + "correo = ?, telefono = ?, primer_nombre = ?, segundo_nombre = ?, "
                + "primer_apellido = ?, segundo_apellido = ?, id_almacen = ? WHERE id_usuario = ?";

        jdbcTemplate.update(sql,
                supervisor.getNombreUsuario(), supervisor.getContrasenia(),
                supervisor.getCorreo(), supervisor.getTelefono(),
                supervisor.getPrimerNombre(), supervisor.getSegundoNombre(),
                supervisor.getPrimerApellido(), supervisor.getSegundoApellido(),
                supervisor.getIdAlmacen(), supervisor.getIdUsuario());

        return supervisor;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM supervisor WHERE id_usuario = ?", id);
    }
}
