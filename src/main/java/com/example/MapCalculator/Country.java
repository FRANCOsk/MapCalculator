package com.example.MapCalculator;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private String name;
    private List<String> borders = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBorders() {
        return borders;
    }

    public void setBorders(List<String> borders) {
        this.borders = borders;
    }
}
