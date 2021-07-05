package com.company;

import java.io.Serializable;

public class checkoutclass implements Serializable{
    public String productname;
    public int product_id,quantity,price,totalPrice;
    checkoutclass(int id,String name,int p,int q,int t){
        productname=name;
        product_id=id;
        quantity=q;
        price=p;
        totalPrice=t;
    }

    public int getProduct_id() {
        return product_id;
    }


    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void inc_quantity(){
        quantity++;
        totalPrice=price*quantity;
    }
    public void dec_quantity(){
        quantity--;
        totalPrice=price*quantity;
    }
    public String getProductName(){
        return productname;
    }
    public int getTotalPrice(){
      //  totalPrice=price*quantity;
        return totalPrice;
    }
    public int getPrice(){
        return price;
    }
    public int getTotalQuantity(){
        return quantity;
    }
    public boolean productmatch(String name){
        if(productname == name){
            return true;
        }
        return false;
    }
}

