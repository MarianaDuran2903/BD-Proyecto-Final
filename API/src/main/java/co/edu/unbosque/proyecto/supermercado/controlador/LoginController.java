package co.edu.unbosque.proyecto.supermercado.controlador;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.LoginRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.LoginResponseDTO;
import co.edu.unbosque.proyecto.supermercado.servicio.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(loginService.login(dto));
    }
}
