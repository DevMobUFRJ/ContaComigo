package com.devmob.contacomigo.ExpandableList;

import com.devmob.contacomigo.model.Food;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DevMachine on 30/03/2017.
 */


public class HeaderInfo {

    Food food;

    private ArrayList<ChildInfo> list = new ArrayList<>();

    public void setFood(Food food) {
        this.food = food;
    }

    public String getFoodName() {
        return food.getName();
    }

    public double getFoodPrice() {
        return food.getPrice();
    }

    public void setFoodName(String name) {
        food.setName(name);
    }

    public ArrayList<ChildInfo> getProductList() {
        return list;
    }

    public void setPeopleList(ArrayList<ChildInfo> people) {
        this.list = people;
    }

}