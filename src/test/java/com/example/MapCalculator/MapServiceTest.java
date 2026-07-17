package com.example.MapCalculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapServiceTest {

    private final MapService mapService = new MapService(new ObjectMapper());

    @Test
    void returnsDirectRouteBetweenNeighbouringCountries() {
        List<String> route = mapService.findRoute("SVK", "CZE").orElseThrow();

        assertEquals(List.of("SVK", "CZE"), route);
    }

    @Test
    void rejectsUnknownCountryCode() {
        assertTrue(mapService.findRoute("UNKNOWN", "CZE").isEmpty());
    }

    @Test
    void rejectsRouteToTheSameCountry() {
        assertTrue(mapService.findRoute("SVK", "svk").isEmpty());
    }
}
