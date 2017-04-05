package com.devmob.contacomigo.ExpandableList;

import java.util.ArrayList;
/**
 * Created by DevMachine on 30/03/2017.
 */


public class HeaderInfo {

    private String name;
    private ArrayList<ChildInfo> list = new ArrayList<ChildInfo>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ChildInfo> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<ChildInfo> productList) {
        this.list = productList;
    }

}