package org.example.maosaobra.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import tools.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Optional<double[]> buscarCoordenadas(String endereco) {
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", endereco)
                    .queryParam("format", "json")
                    .queryParam("limit", 1)
                    .queryParam("countrycodes", "br")
                    .build().encode().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "MaosaObra-TCC");

            JsonNode resposta = restTemplate
                    .exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class)
                    .getBody();

            if (resposta == null || resposta.size() == 0) {
                return Optional.empty();
            }

            double lat = resposta.get(0).get("lat").asDouble();
            double lon = resposta.get(0).get("lon").asDouble();
            return Optional.of(new double[]{lat, lon});

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
