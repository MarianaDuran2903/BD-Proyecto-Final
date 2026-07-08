package co.edu.unbosque.proyecto.supermercado.repositorio;

import java.util.List;

import co.edu.unbosque.proyecto.supermercado.modelo.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {

    List<Almacen> findByUbicacionCiudad(String ubicacionCiudad);
}