package com.example.backend.modules.compartido.infrastructure.client;

import com.example.backend.modules.compartido.domain.ports.out.CatedraIntegracion;
import com.example.backend.modules.eventos.domain.model.Evento;
import com.example.backend.modules.eventos.infrastructure.persistence.entity.EventoEntity;
import com.example.backend.modules.eventos.infrastructure.persistence.mapper.EventoMapper;
import com.example.backend.modules.ventas.domain.model.AsientoVendido;
import com.example.backend.modules.ventas.domain.model.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CatedraRestAdapter implements CatedraIntegracion {
    private final RestTemplate restTemplate;
    private final EventoMapper eventoMapper;

    @Value("${app.catedra.url}")
    private String baseUrl;

    @Value("${app.catedra.token}")
    private String authToken;

    @Override
    public List<Evento> obtenerEventos() {
        String url = baseUrl + "/eventos";

        ResponseEntity<List<EventoEntity>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                crearHttpEntity(null),
                new ParameterizedTypeReference<List<EventoEntity>>() {}
        );

        if (response.getBody() == null) {
            return List.of();
        }

        return response.getBody().stream()
                .map(eventoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean bloquearAsientos(Long eventoId, Set<AsientoVendido> asientos) {
        String url = baseUrl + "/bloquear-asientos";

        BloqueoRequest bloqueoRequest = new BloqueoRequest(
                eventoId,
                asientos.stream()
                        .map(asientoVendido ->
                                new AsientoDto(
                                        asientoVendido.getFila(),
                                        asientoVendido.getColumna()))
                        .toList()
        );

        try {
            ResponseEntity<BloqueoResponse> bloqueoResponse = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    crearHttpEntity(bloqueoRequest),
                    BloqueoResponse.class
            );

            return bloqueoResponse.getBody() != null && bloqueoResponse.getBody().resultado();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long confirmarVenta(Venta venta) {
        String url = baseUrl + "/realizar-venta";

        VentaRequest ventaRequest = new VentaRequest(
                venta.getEventoId(),
                venta.getFechaVenta().toString(),
                venta.getPrecioVenta(),
                venta.getAsientosVendidos().stream()
                        .map(asientoVendido ->
                                new AsientoVentaDto(
                                        asientoVendido.getFila(),
                                        asientoVendido.getColumna(),
                                        asientoVendido.getPersona()))
                        .toList()
        );

        try {
            ResponseEntity<VentaResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    crearHttpEntity(ventaRequest),
                    VentaResponse.class
            );

            if (response.getBody() != null && response.getBody().resultado()) {
                return response.getBody().ventaId();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> HttpEntity<T> crearHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authToken);
        return new HttpEntity<>(body, headers);
    }

    record BloqueoRequest(Long eventoId, List<AsientoDto> asientos) {}
    record AsientoDto(int fila, int columna) {}
    record BloqueoResponse(boolean resultado, String descripcion, Long eventoId, List<AsientoResponseDto> asientos) {}
    record AsientoResponseDto(String estado, int fila, int columna) {}

    record VentaRequest(Long eventoId, String fecha, double precioVenta, List<AsientoVentaDto> asientos) {}
    record AsientoVentaDto(int fila, int columna, String persona) {}
    record VentaResponse(Long eventoId, Long ventaId, String fechaVenta, List<AsientoVentaResponseDto> asientos, boolean resultado, String descripcion, double precioVenta) {}
    record AsientoVentaResponseDto(int fila, int columna, String persona, String estado) {}
}
