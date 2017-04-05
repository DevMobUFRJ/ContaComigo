package com.devmob.contacomigo.model;

/**
 * Created by silviomm on 05/04/17.
 */

public class Person {

    private int id;
    private String name;
    private double totalPrice;

    //TODO remove id from constructor, must be auto-incremented(sql)
    public Person(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setName(String name) {
        this.name = name;
    }
}
