package com.example.MapCalculator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routing")
public class Controller {

    private final MapService mapService;

    public Controller(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/{origin}/{destination}")
    public ResponseEntity<Route> getRoute(
            @PathVariable String origin,
            @PathVariable String destination) {

        return mapService.findRoute(origin, destination)
                .map(routeItems -> {
                    Route route = new Route();
                    route.setRoute(routeItems);
                    return ResponseEntity.ok(route);
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
