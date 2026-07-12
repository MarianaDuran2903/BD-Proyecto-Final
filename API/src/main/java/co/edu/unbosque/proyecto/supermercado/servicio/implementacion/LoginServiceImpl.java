package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import co.edu.unbosque.proyecto.supermercado.modelo.Almacen;
import co.edu.unbosque.proyecto.supermercado.modelo.Cliente;
import co.edu.unbosque.proyecto.supermercado.modelo.Pareja;
import co.edu.unbosque.proyecto.supermercado.modelo.Supervisor;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.LoginRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.LoginResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.AlmacenRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.ClienteRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.ParejaRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.SupervisorRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.LoginService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final ClienteRepository clienteRepository;
    private final ParejaRepository parejaRepository;
    private final SupervisorRepository supervisorRepository;
    private final AlmacenRepository almacenRepository;

    public LoginServiceImpl(ClienteRepository clienteRepository,
                            ParejaRepository parejaRepository,
                            SupervisorRepository supervisorRepository,
                            AlmacenRepository almacenRepository) {
        this.clienteRepository = clienteRepository;
        this.parejaRepository = parejaRepository;
        this.supervisorRepository = supervisorRepository;
        this.almacenRepository = almacenRepository;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Long id = dto.getIdUsuario();
        String contrasenia = dto.getContrasenia();

        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            Cliente c = clienteOpt.get();
            if (!contrasenia.equals(c.getContrasenia())) {
                throw new ReglaNegocioException("Credenciales incorrectas");
            }
            return fromCliente(c);
        }

        Optional<Pareja> parejaOpt = parejaRepository.findById(id);
        if (parejaOpt.isPresent()) {
            Pareja p = parejaOpt.get();
            if (!contrasenia.equals(p.getContrasenia())) {
                throw new ReglaNegocioException("Credenciales incorrectas");
            }
            return fromPareja(p);
        }

        Optional<Supervisor> supervisorOpt = supervisorRepository.findById(id);
        if (supervisorOpt.isPresent()) {
            Supervisor s = supervisorOpt.get();
            if (!contrasenia.equals(s.getContrasenia())) {
                throw new ReglaNegocioException("Credenciales incorrectas");
            }
            Almacen almacen = almacenRepository.findById(s.getIdAlmacen()).orElse(null);
            return fromSupervisor(s, almacen);
        }

        throw new ReglaNegocioException("No existe un usuario registrado con el número de cédula " + id);
    }

    private LoginResponseDTO fromCliente(Cliente c) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setTipo("CLIENTE");
        dto.setIdUsuario(c.getIdUsuario());
        dto.setNombreUsuario(c.getNombreUsuario());
        dto.setPrimerNombre(c.getPrimerNombre());
        dto.setSegundoNombre(c.getSegundoNombre());
        dto.setPrimerApellido(c.getPrimerApellido());
        dto.setSegundoApellido(c.getSegundoApellido());
        dto.setEstado(c.getEstado());
        dto.setCupoPropio(c.getCupoPropio());
        dto.setCupoTotalAutorizado(c.getCupoTotalAutorizado());
        return dto;
    }

    private LoginResponseDTO fromPareja(Pareja p) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setTipo("PAREJA");
        dto.setIdUsuario(p.getIdUsuario());
        dto.setNombreUsuario(p.getNombreUsuario());
        dto.setPrimerNombre(p.getPrimerNombre());
        dto.setSegundoNombre(p.getSegundoNombre());
        dto.setPrimerApellido(p.getPrimerApellido());
        dto.setSegundoApellido(p.getSegundoApellido());
        dto.setEstado(p.getEstado());
        dto.setCupoAsignado(p.getCupoAsignado());
        dto.setIdUsuarioCliente(p.getIdUsuarioCliente());
        return dto;
    }

    private LoginResponseDTO fromSupervisor(Supervisor s, Almacen almacen) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setTipo("SUPERVISOR");
        dto.setIdUsuario(s.getIdUsuario());
        dto.setNombreUsuario(s.getNombreUsuario());
        dto.setPrimerNombre(s.getPrimerNombre());
        dto.setSegundoNombre(s.getSegundoNombre());
        dto.setPrimerApellido(s.getPrimerApellido());
        dto.setSegundoApellido(s.getSegundoApellido());
        dto.setEstado(s.getEstado());
        dto.setIdAlmacen(s.getIdAlmacen());
        if (almacen != null) {
            dto.setNombreAlmacen(almacen.getNombreAlmacen());
        }
        return dto;
    }
}
