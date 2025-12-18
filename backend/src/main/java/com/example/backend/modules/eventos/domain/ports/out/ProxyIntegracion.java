package com.example.backend.modules.eventos.domain.ports.out;

import java.util.Map;

public interface ProxyIntegracion {
    Map<String, String> obtenerEstadoAsientos(Long eventoId);
}
