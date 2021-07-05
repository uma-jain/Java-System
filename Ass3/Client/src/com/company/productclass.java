package com.company;

import java.io.Serializable;
import java.util.Objects;

public class productclass implements Serializable {
    public String name,configuration,colour;
    public int quantity,price,id;

    public productclass( int id,String name,int price,int quantity, String colour,String configuration) {
        this.name = name;
        this.configuration = configuration;
        this.colour = colour;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
    }

    public boolean productmatch(int i) {
        if(id == i){
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
