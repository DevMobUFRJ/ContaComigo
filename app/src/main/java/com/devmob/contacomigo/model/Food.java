package com.devmob.contacomigo.model;

/**
 * Created by silviomm on 05/04/17.
 */

public class Food {

    private int id;
    private String name;
    private double price;


    // TODO remove id from constructor, must be auto-incremented
    public Food(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
}
