package com.example.MapCalculator;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RequestMapping(value = "/routing")
@RestController
public class Controller {

@Autowired
MapService mapService;

@GetMapping (value= "{origin}/{destination}")
public ResponseEntity getRoute(@PathVariable String origin, @PathVariable String destination) throws IOException, ParseException
{
  List<String> response = mapService.getRoute(origin,destination);

  if (response.isEmpty()){

    return new ResponseEntity(HttpStatus.BAD_REQUEST);
  }

  Route route = new Route();

  route.setRoute(response);

  return new ResponseEntity(route, HttpStatus.OK);
}


}

