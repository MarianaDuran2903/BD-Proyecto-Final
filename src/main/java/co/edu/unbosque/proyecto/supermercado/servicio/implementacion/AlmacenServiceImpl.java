package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.Almacen;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.AlmacenDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.repositorio.AlmacenRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.AlmacenService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlmacenServiceImpl implements AlmacenService {

    private final AlmacenRepository almacenRepository;
    private final ModelMapper mm = new ModelMapper();

    public AlmacenServiceImpl(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    @Override
    @Transactional
    public AlmacenDTO crear(AlmacenDTO dto) {
        Almacen almacen = mm.map(dto, Almacen.class);
        almacen.setIdAlmacen(null);
        Almacen guardado = almacenRepository.save(almacen);
        return mm.map(guardado, AlmacenDTO.class);
    }

    @Override
    public AlmacenDTO obtenerPorId(Long idAlmacen) {
        return mm.map(buscarPorIdOLanzarError(idAlmacen), AlmacenDTO.class);
    }

    @Override
    public List<AlmacenDTO> listarTodos() {
        return almacenRepository.findAll().stream()
                .map(almacen -> mm.map(almacen, AlmacenDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AlmacenDTO actualizar(Long idAlmacen, AlmacenDTO dto) {
        Almacen almacen = buscarPorIdOLanzarError(idAlmacen);
        almacen.setNombreAlmacen(dto.getNombreAlmacen());
        almacen.setUbicacionCiudad(dto.getUbicacionCiudad());
        almacen.setUbicacionAvenida(dto.getUbicacionAvenida());
        almacen.setUbicacionCalle(dto.getUbicacionCalle());
        return mm.map(almacenRepository.save(almacen), AlmacenDTO.class);
    }

    @Override
    @Transactional
    public void eliminar(Long idAlmacen) {
        almacenRepository.delete(buscarPorIdOLanzarError(idAlmacen));
    }

    private Almacen buscarPorIdOLanzarError(Long idAlmacen) {
        return almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un almacén con id " + idAlmacen));
    }
}
