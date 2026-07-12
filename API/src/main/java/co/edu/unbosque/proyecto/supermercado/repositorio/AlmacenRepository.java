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

    private static final String COLUMNAS_RESUMEN =
            "id_almacen, nombre_almacen, ubicacion_ciudad";

    private final RowMapper<Almacen> mapperResumen = (rs, rowNum) -> {
        Almacen a = new Almacen();
        a.setIdAlmacen(rs.getLong("id_almacen"));
        a.setNombreAlmacen(rs.getString("nombre_almacen"));
        a.setUbicacionCiudad(rs.getString("ubicacion_ciudad"));
        return a;
    };

    public List<Almacen> findAll() {
        String sql = "SELECT " + COLUMNAS_RESUMEN + " FROM almacen ORDER BY id_almacen";
        return jdbcTemplate.query(sql, mapperResumen);
    }

    public Optional<Almacen> findById(Long id) {
        String sql = "SELECT " + COLUMNAS_RESUMEN + " FROM almacen WHERE id_almacen = ?";
        List<Almacen> resultado = jdbcTemplate.query(sql, mapperResumen, id);
        return resultado.stream().findFirst();
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
}