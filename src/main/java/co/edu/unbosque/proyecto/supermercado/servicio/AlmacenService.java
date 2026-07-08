package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.AlmacenDTO;

import java.util.List;

public interface AlmacenService {

    AlmacenDTO crear(AlmacenDTO dto);

    AlmacenDTO obtenerPorId(Long idAlmacen);

    List<AlmacenDTO> listarTodos();

    AlmacenDTO actualizar(Long idAlmacen, AlmacenDTO dto);

    void eliminar(Long idAlmacen);
}