package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.proyecto.supermercado.modelo.Almacen;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AlmacenRepository {

    private final JdbcTemplate jdbcTemplate;

    public AlmacenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String COLUMNAS =
            "id_almacen, nombre_almacen, ubicacion_ciudad, ubicacion_avenida, ubicacion_calle";

    private final RowMapper<Almacen> mapper = (rs, rowNum) -> {
        Almacen a = new Almacen();
        a.setIdAlmacen(rs.getLong("id_almacen"));
        a.setNombreAlmacen(rs.getString("nombre_almacen"));
        a.setUbicacionCiudad(rs.getString("ubicacion_ciudad"));
        a.setUbicacionAvenida(rs.getString("ubicacion_avenida"));
        a.setUbicacionCalle(rs.getString("ubicacion_calle"));
        return a;
    };

    public List<Almacen> findAll() {
        String sql = "SELECT " + COLUMNAS + " FROM almacen ORDER BY id_almacen";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Almacen> findById(Long id) {
        String sql = "SELECT " + COLUMNAS + " FROM almacen WHERE id_almacen = ?";
        List<Almacen> resultado = jdbcTemplate.query(sql, mapper, id);
        return resultado.stream().findFirst();
    }

    public List<Almacen> findByUbicacionCiudad(String ubicacionCiudad) {
        String sql = "SELECT " + COLUMNAS + " FROM almacen WHERE ubicacion_ciudad = ?";
        return jdbcTemplate.query(sql, mapper, ubicacionCiudad);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM almacen WHERE id_almacen = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return total != null && total > 0;
    }

    public Almacen save(Almacen almacen) {
        String sql = "INSERT INTO almacen (nombre_almacen, ubicacion_ciudad, ubicacion_avenida, ubicacion_calle) "
                + "VALUES (?, ?, ?, ?) RETURNING id_almacen";

        Long idGenerado = jdbcTemplate.queryForObject(sql, Long.class,
                almacen.getNombreAlmacen(),
                almacen.getUbicacionCiudad(),
                almacen.getUbicacionAvenida(),
                almacen.getUbicacionCalle());

        almacen.setIdAlmacen(idGenerado);
        return almacen;
    }

    public Almacen update(Almacen almacen) {
        String sql = "UPDATE almacen SET nombre_almacen = ?, ubicacion_ciudad = ?, "
                + "ubicacion_avenida = ?, ubicacion_calle = ? WHERE id_almacen = ?";

        jdbcTemplate.update(sql,
                almacen.getNombreAlmacen(),
                almacen.getUbicacionCiudad(),
                almacen.getUbicacionAvenida(),
                almacen.getUbicacionCalle(),
                almacen.getIdAlmacen());

        return almacen;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM almacen WHERE id_almacen = ?", id);
    }
}