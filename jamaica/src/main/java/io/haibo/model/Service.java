package io.haibo.model;

import java.util.List;

public class Service {
    private int id;

    private String name;

    private String description;

    private String timeTypes;

    private double rate;

    private int limits;

    public Service() {}

    public Service(int id, String name, String description, String timeTypes, double rate, int limits) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeTypes = timeTypes;
        this.rate = rate;
        this.limits = limits;
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

    public String getTimeTypes() {
        return timeTypes;
    }

    public void setTimeTypes(String timeTypes) {
        this.timeTypes = timeTypes;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getLimits() {
        return limits;
    }

    public void setLimits(int limits) {
        this.limits = limits;
    }

    @Override
    public String toString() {
        return String.format("name : %s\tdescrption : %s\ttime_type : %s\trate : %4.2f\tlimits: %d",
                this.name, this.description, this.timeTypes, this.rate, this.limits);
    }
}
