package com.example.MapCalculator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class MapService {

    private static final String COUNTRIES_RESOURCE = "static/countries.json";

    private final Map<String, Country> countriesByCode;

    public MapService(ObjectMapper objectMapper) {
        this.countriesByCode = loadCountries(objectMapper);
    }

    public Optional<List<String>> findRoute(String origin, String destination) {
        String originCode = normalizeCode(origin);
        String destinationCode = normalizeCode(destination);

        if (originCode.isBlank() || destinationCode.isBlank() || originCode.equals(destinationCode)) {
            return Optional.empty();
        }

        Country originCountry = countriesByCode.get(originCode);
        Country destinationCountry = countriesByCode.get(destinationCode);

        if (originCountry == null || destinationCountry == null) {
            return Optional.empty();
        }

        Graph<Country, DefaultEdge> graph = createGraph();
        GraphPath<Country, DefaultEdge> path =
                new DijkstraShortestPath<>(graph).getPath(originCountry, destinationCountry);

        if (path == null) {
            return Optional.empty();
        }

        return Optional.of(path.getVertexList().stream()
                .map(Country::getName)
                .toList());
    }

    private Graph<Country, DefaultEdge> createGraph() {
        Graph<Country, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        countriesByCode.values().forEach(graph::addVertex);

        for (Country country : countriesByCode.values()) {
            for (String borderCode : country.getBorders()) {
                Country neighbour = countriesByCode.get(normalizeCode(borderCode));

                if (neighbour != null
                        && neighbour != country
                        && !graph.containsEdge(country, neighbour)) {
                    graph.addEdge(country, neighbour);
                }
            }
        }

        return graph;
    }

    private Map<String, Country> loadCountries(ObjectMapper objectMapper) {
        ClassPathResource resource = new ClassPathResource(COUNTRIES_RESOURCE);

        try (InputStream inputStream = resource.getInputStream()) {
            JsonNode root = objectMapper.readTree(inputStream);

            if (!root.isArray()) {
                throw new IllegalStateException("Countries resource must contain a JSON array.");
            }

            Map<String, Country> countries = new LinkedHashMap<>();

            for (JsonNode countryNode : root) {
                String code = normalizeCode(countryNode.path("cca3").asText());
                if (code.isBlank()) {
                    continue;
                }

                Country country = new Country();
                country.setName(code);

                List<String> borders = new ArrayList<>();
                JsonNode bordersNode = countryNode.path("borders");
                if (bordersNode.isArray()) {
                    bordersNode.forEach(border -> borders.add(normalizeCode(border.asText())));
                }

                country.setBorders(borders);
                countries.put(code, country);
            }

            return Collections.unmodifiableMap(countries);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load country routing data.", exception);
        }
    }

    private String normalizeCode(String code) {
        return code == null ? "" : code.trim().toUpperCase(Locale.ROOT);
    }
}
