package co.edu.unbosque.proyecto.supermercado.servicio;


import co.edu.unbosque.proyecto.supermercado.modelo.dto.AutorizacionSobrecupoRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.AutorizacionSobrecupoResponseDTO;

public interface AutorizacionSobrecupoService {

    AutorizacionSobrecupoResponseDTO crear(AutorizacionSobrecupoRequestDTO dto);

    AutorizacionSobrecupoResponseDTO obtenerPorCompra(Long idCompra);
}
