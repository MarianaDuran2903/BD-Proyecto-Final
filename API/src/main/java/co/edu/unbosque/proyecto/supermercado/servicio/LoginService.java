package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.LoginRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.LoginResponseDTO;

public interface LoginService {
    LoginResponseDTO login(LoginRequestDTO dto);
}
