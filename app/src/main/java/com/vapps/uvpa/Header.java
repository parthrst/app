package com.vapps.uvpa;

import java.util.ArrayList;

public class Header {
    public String brand;
    public String model;
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<body> list=new ArrayList<>();
    public ArrayList<body> getList() {
        return list;
    }

    public void setList(ArrayList<body> list) {
        this.list = list;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    }
