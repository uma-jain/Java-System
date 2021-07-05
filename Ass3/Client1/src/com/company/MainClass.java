package com.company;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MainClass implements Serializable {
    ArrayList<checkoutclass> cart = new ArrayList<checkoutclass>();

    ArrayList<productclass> productList = new ArrayList<productclass>();
    static int  num,total,total1;
    int choice;


    MainClass(ArrayList<checkoutclass> cart, ArrayList<productclass> productList) {
        this.cart = cart;
        this.productList = productList;
    }

    public MainClass() {

    }

    void addToProductList(productclass p){
        productList.add(p);
    }
    void empty_productList(){
        productList.clear();
    }
    void addToCart(checkoutclass c){
        cart.add(c);
    }
    void empty_cart(){
        cart.clear();
    }
    void display() {
        System.out.println("Products");
        //diaplay al
        for(productclass pr: productList){
            System.out.println(pr.getName());

        }
    }
    void displayDetaileddata(){
        System.out.printf("\n%2s %20s %25s %20s %25s", "ITEM ID", "NAME", "PRICE", "COLOR", "CONFIGURATION\n");
        for(productclass pr: productList)
        {
            System.out.format("%2s %30s %20s %20s %25s",pr.getId() , pr.getName() ,pr.getPrice(),pr.getColour(),pr.getConfiguration()+"\n");

        }
    }
    boolean buy(int id,int q)
    {
        return false;
    }

    void check() {
        System.out.println("---------------Availability----------------");

        System.out.printf("\n%20s %25s ", "PRODUCT NAME", "QUANTITY","\n");
        for(productclass pr: productList)
        {
            System.out.printf("\n%20s %20s ",pr.getName(),pr.getQuantity());
        }
    }
    void checkout(){
        System.out.println("\nYOUR CART");
        total=0;
        System.out.printf("\n%2s %25s %20s %20s %25s ","ITEM ID", "PRODUCT NAME", "QUANTITY", "PRICE", "TOTAL PRICE\n");
        for(checkoutclass pr: cart){
            System.out.printf("\n%2s %30s %20s %20s %25s",pr.getProduct_id(),pr.getProductName(),pr.getTotalQuantity(), pr.getPrice(), pr.getTotalPrice()+"\n");
            total=total+pr.getTotalPrice();
        }
        System.out.println("\n-------------------------------------------------------------------------------------------------");
        System.out.println("\nTOTAL PRICE TO PAY : RS "+total);


    }
    void bill(){
        total=0;
        System.out.println("\n------------------------------ BILL -------------------------------------------");
        System.out.printf("\n%25s %20s %20s %25s ", "PRODUCT NAME", "QUANTITY", "PRICE", "TOTAL PRICE\n");

        for(checkoutclass pr: cart){
            System.out.printf("\n%30s %20s %20s %25s ",pr.getProductName(),pr.getTotalQuantity(), pr.getPrice(), pr.getTotalPrice()+"\n");
            total=total+pr.getTotalPrice();
        }
        System.out.println("\n------------------------------------------------------------------------------");
        System.out.println("\nTOTAL : RS "+total);

    }


}

