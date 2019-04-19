package io.haibo.model;

import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name="Service.getByName", query="select s from Service s where s.name = :name"),
    @NamedQuery(name="Service.getAll", query="select * from Service s"),
})
@Table(name="service")
public class Service {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="min")
    private int min;

    @Column(name="max")
    private int max;

    @Column(name="rate")
    private double rate;

    @Column(name="limits")
    private int limits;

    public Service() {}

    public Service(String name, String description, int min, int max, double rate, int limits) {
        this.name = name;
        this.description = description;
        this.min = min;
        this.max = max;
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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
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
                this.name, this.description, this.min, this.rate, this.limits);
    }
}
