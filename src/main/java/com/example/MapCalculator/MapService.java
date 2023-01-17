package com.example.MapCalculator;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class MapService {

    Map<String,Country> countryMap;
    DijkstraShortestPath<Country,DefaultEdge> dijkstraShortestPath;
    List<String> getRoute(String origin, String destination) throws IOException, ParseException {

        List<Country> country = new ArrayList<>();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/resources/static/countries.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray countryList = (JSONArray) obj;
            System.out.println(countryList);

            countryList.forEach(countryJSON -> country.add(parseCountryObject((JSONObject) countryJSON)));
        }

        Country originCountry = country.stream().filter(item -> item.getName().toLowerCase().equals(origin.toLowerCase())).findAny().orElse(new Country());
        Country destinationCountry = country.stream().filter(item -> item.getName().toLowerCase().equals(destination.toLowerCase())).findAny().orElse(new Country());

        if (!country.contains(originCountry) || !country.contains(destinationCountry) || origin.toLowerCase().equals(destination.toLowerCase()) ){

            return new ArrayList<String>();

        }

         createGraph(country);

        final GraphPath<Country, DefaultEdge> path = dijkstraShortestPath.getPath(originCountry, destinationCountry);

        if(path==null){

            return new ArrayList<String>();

        }

        List<String> result = new ArrayList<String>();

                path.getVertexList().stream().forEach(item->result.add(item.getName()));

        return result;

    }

    private void createGraph(List<Country> countryList){

        countryMap = countryList.stream().collect(Collectors.toMap(Country::getName,item->item));

        final Graph<Country, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);

        try {
            countryMap.values().forEach(graph::addVertex);
            countryMap.values().stream()
                    .map(this::getEdges)
                    .flatMap(Collection::stream)
                    .forEach(edge -> graph.addEdge(edge.getFirst(), edge.getSecond()));
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unable to construct a graph from countries", e);
        }

        dijkstraShortestPath = new DijkstraShortestPath<>(graph);


    }
    private List<Pair<Country, Country>> getEdges(final Country country) {
        return country.getBorders().stream()
                .map(countryMap::get)
                .map(neighbour -> new Pair<>(country, neighbour))
                .collect(Collectors.toList());
    }


    private Country parseCountryObject(JSONObject countryJSON) {

        {
            Country country = new Country();

            country.setName((String) (countryJSON.get("cca3")));

            JSONArray borderJSON = (JSONArray) countryJSON.get("borders");

            List<String> borders = new ArrayList<String>();

            borderJSON.forEach(border -> {

                borders.add((String) border);

            });

            country.setBorders(borders);

            return country;
        }

    }

}