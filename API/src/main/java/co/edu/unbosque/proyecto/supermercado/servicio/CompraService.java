package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraResponseDTO;

import java.util.List;

public interface CompraService {

    CompraResponseDTO registrar(CompraRequestDTO dto);

    CompraResponseDTO obtenerPorId(Long idCompra);

    List<CompraResponseDTO> listarPorPareja(Long idUsuarioPareja);
}