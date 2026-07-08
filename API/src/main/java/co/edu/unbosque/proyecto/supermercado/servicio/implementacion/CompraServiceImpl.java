package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.Almacen;
import co.edu.unbosque.proyecto.supermercado.modelo.Compra;
import co.edu.unbosque.proyecto.supermercado.modelo.Pareja;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.DiaSemanaUtil;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.AlmacenRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.CompraRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.ParejaRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.RestriccionHorarioRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.CompraService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ParejaRepository parejaRepository;
    private final AlmacenRepository almacenRepository;
    private final RestriccionHorarioRepository restriccionHorarioRepository;
    private final ModelMapper mm = new ModelMapper();

    public CompraServiceImpl(CompraRepository compraRepository,
                             ParejaRepository parejaRepository,
                             AlmacenRepository almacenRepository,
                             RestriccionHorarioRepository restriccionHorarioRepository) {
        this.compraRepository = compraRepository;
        this.parejaRepository = parejaRepository;
        this.almacenRepository = almacenRepository;
        this.restriccionHorarioRepository = restriccionHorarioRepository;
    }

    @Override
    @Transactional
    public CompraResponseDTO registrar(CompraRequestDTO dto) {
        Pareja pareja = parejaRepository.findById(dto.getIdUsuarioPareja())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la pareja con id " + dto.getIdUsuarioPareja()));

        Almacen almacen = almacenRepository.findById(dto.getIdAlmacen())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el almacén con id " + dto.getIdAlmacen()));

        // Regla de negocio: la pareja puede estar bloqueada en este día
        // y hora específicos, por petición del cliente titular.
        String diaSemana = DiaSemanaUtil.aEspanol(dto.getFecha().getDayOfWeek());
        boolean bloqueada = restriccionHorarioRepository.existeBloqueoActivo(
                pareja.getIdUsuario(), diaSemana, dto.getHora());

        if (bloqueada) {
            throw new ReglaNegocioException(
                    "La pareja tiene una restricción configurada para " + diaSemana
                            + " a las " + dto.getHora() + ". La compra no puede realizarse.");
        }

        // Regla de negocio: si el monto supera el cupo disponible de la
        // pareja, la compra se guarda igual, pero queda marcada como
        // pendiente de sobrecupo (requiere_sobrecupo = true). El
        // supervisor deberá aprobarla luego mediante AutorizacionSobrecupo.
        BigDecimal yaConsumido = compraRepository.sumarMontoPorPareja(pareja.getIdUsuario());
        BigDecimal cupoDisponible = pareja.getCupoAsignado().subtract(yaConsumido);
        boolean requiereSobrecupo = dto.getMonto().compareTo(cupoDisponible) > 0;

        Compra compra = new Compra();
        compra.setMonto(dto.getMonto());
        compra.setFecha(dto.getFecha());
        compra.setHora(dto.getHora());
        compra.setRequiereSobrecupo(requiereSobrecupo);
        compra.setPareja(pareja);
        compra.setAlmacen(almacen);

        Compra guardada = compraRepository.save(compra);
        return mapearAResponseDTO(guardada);
    }

    @Override
    public CompraResponseDTO obtenerPorId(Long idCompra) {
        return mapearAResponseDTO(buscarPorIdOLanzarError(idCompra));
    }

    @Override
    public List<CompraResponseDTO> listarPorPareja(Long idUsuarioPareja) {
        return compraRepository.findByPareja_IdUsuario(idUsuarioPareja).stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    private Compra buscarPorIdOLanzarError(Long idCompra) {
        return compraRepository.findById(idCompra)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la compra con id " + idCompra));
    }

    private CompraResponseDTO mapearAResponseDTO(Compra compra) {
        CompraResponseDTO dto = mm.map(compra, CompraResponseDTO.class);
        dto.setIdUsuarioPareja(compra.getPareja().getIdUsuario());
        dto.setNombreParejaCompleto(
                compra.getPareja().getPrimerNombre() + " " + compra.getPareja().getPrimerApellido());
        dto.setIdAlmacen(compra.getAlmacen().getIdAlmacen());
        dto.setNombreAlmacen(compra.getAlmacen().getNombreAlmacen());

        if (compra.getSupervisor() != null) {
            dto.setIdUsuarioSupervisor(compra.getSupervisor().getIdUsuario());
            dto.setNombreSupervisorCompleto(
                    compra.getSupervisor().getPrimerNombre() + " " + compra.getSupervisor().getPrimerApellido());
        }
        return dto;
    }
}