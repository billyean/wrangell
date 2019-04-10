package io.haibo.model;

import java.util.List;

public class Service {
    private int id;

    private String name;

    private String description;

    private List<Integer> timeTypes;

    private double rate;

    private int limit;

    public Service(int id, String name, String description, List<Integer> timeTypes, double rate, int limit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeTypes = timeTypes;
        this.rate = rate;
        this.limit = limit;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getTimeTypes() {
        return timeTypes;
    }

    public void setTimeTypes(List<Integer> timeTypes) {
        this.timeTypes = timeTypes;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
