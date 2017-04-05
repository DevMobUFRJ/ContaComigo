package com.devmob.contacomigo.ExpandableList;

import com.devmob.contacomigo.model.Person;


/**
 * Created by DevMachine on 30/03/2017.
 */

public class ChildInfo {

    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setPersonName(String name){
        this.person.setName(name);
    }

    public String getPersonName(){
        return person.getName();
    }

    public double getPrice() {
        return person.getTotalPrice();
    }

    public void setPrice(double price) {
        person.setTotalPrice(price);
    }

}