package com.company;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class Main {

    //add jar library in depedency
//create database and two tables
//insert values in product table
/*
create database sdl;
use sdl;
create table product(
item_id INT NOT NULL AUTO_INCREMENT,
product_name varchar(30),
price INT,
quantity INT,
colour VARCHAR(15),
config VARCHAR(10),
Primary key(item_id)
);
INSERT INTO product values(null,"Realme Narrzo 20 pro",14999,7,"Black Ninja","6GB+64GB");
INSERT INTO product values(null,"Realme 7i",19999,7,"Fusion Blue","4GB+64GB");
INSERT INTO product values(null,"Realme 7 pro",14999,7,"Mirror Green","6GB+128GB");
INSERT INTO product values(null,"Realme 5 pro",19999,7,"Chroma White","6GB+32GB");
INSERT INTO product values(null,"Realme XT",17999,7,"Space Blue","4GB+64GB");
INSERT INTO product values(null,"Realme X",20999,7,"Pearl Violet","4GB+128GB");
select * from product;
* */

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        DataInputStream datainput=null;
        DataOutputStream dataoutput=null;
        ObjectInputStream  objinput=null;
        BufferedReader br=null;
        //link socket

        Socket sk = new Socket("localhost",7899);
        System.out.println("Client started");

        datainput  = new DataInputStream(sk.getInputStream()); // get ip from connected socket
        dataoutput    = new DataOutputStream(sk.getOutputStream()); //give output to connected socket
        br=new BufferedReader(new InputStreamReader(System.in));

        // get the output stream from the socket.
        OutputStream outputStream = sk.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // get the input stream from the connected socket
        InputStream inputStream = sk.getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);


        // write your code here
        Scanner s = new Scanner(System.in);
        char proceed;
        MainClass a=new MainClass();
        int choice;
        System.out.println("\n------------------REALME STORE-----------------------");
        do{
            System.out.println("\nENTER APPROPRIATE CHOICE :- ");
            System.out.println("\n ENTER  \n 1 FOR All PRODUCTS \n 2 FOR PRODUCT AVAILABILITY\n 3 TO BUY \n 4 FOR CHECKOUT \n 5. FOR  BILL");
            choice = s.nextInt();
            dataoutput.writeInt(choice);

            switch(choice){
                case 1: {
                    //get obj from server and display data product names
                    try {
                        ((MainClass) objectInputStream.readObject()).display();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                    break;
                case 2: {
                    //display availability
                    try {
                        ((MainClass) objectInputStream.readObject()).check();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                    break;
                case 3: {
                    //actual data including features
                    try {
                        ((MainClass) objectInputStream.readObject()).displayDetaileddata();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    //take id and quantity input na dpass it to server do changes and give success failure result
                    int id, q;
                    System.out.println("\nEnter Item id  you want to buy:");
                    id = sc.nextInt();
                    System.out.println("Enter quantity:");
                    q = sc.nextInt();

                    dataoutput.writeInt(id);
                    dataoutput.writeInt(q);

                    //acknowledgement
                    System.out.println(datainput.readUTF());
                    //System.out.println("complete");
                }
                    break;

                case 4:
                {
                    //get obj and display cart
                    try {
                        int choice_for_edit_procees;
                        ((MainClass)objectInputStream.readObject()).checkout();

                        //show edit process option
                       /* System.out.println(" 1. TO PROCEED");
                        System.out.println(" 2. TO EDIT CART");
                        choice_for_edit_procees=sc.nextInt();

                        dataoutput.writeInt(choice_for_edit_procees);

                        //delete or change quantity
                        int del_or_edit,item_no,inc_or_dec;
                        if(choice_for_edit_procees == 2){

                            System.out.println("Enter\n" +
                                    "1 TO DELETE ITEM FROM CART\n" +
                                    "2 TO CHANGE QUANTITY");

                            del_or_edit=sc.nextInt();
                            dataoutput.writeInt(del_or_edit);

                            switch (del_or_edit){
                                //delete
                                case 1:
                                    System.out.println("ENTER ITEM NO TO DELETE");
                                    item_no=sc.nextInt();
                                    //pass this no to server
                                    dataoutput.writeInt(item_no);
                                    //success msg for deletion
                                    System.out.println("item deleted successfully");
                                    break;
                                case 2:
                                    //edit
                                    System.out.println("ENTER ITEM NO");
                                    item_no=sc.nextInt();
                                    System.out.println("ENTER \n   1.INCREMENT QUANTITY\n   2.DECREMENT QUANTITY");
                                    inc_or_dec=sc.nextInt();
                                    dataoutput.writeInt(item_no);
                                    dataoutput.writeInt(inc_or_dec);

                                    //success msg for
                                    System.out.println("item edited successfully");
                                    //write function in data class to inc or dec item from cart
                                    break;
                            }



                        }*/


                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
              }
                    break;
                case 5: {
                    //get obj and display bill
                    System.out.println("THANKYOU FOR SHOPPING,SEEE YOU SOON!");
                    try {
                        ((MainClass) objectInputStream.readObject()).bill();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    //set a flag that if bill is paid or not ,if yes dont show do you want to continue
                    break;
                }
            }

            System.out.println("\nDO YOU WANT TO CONTINUE (Y/N)");
            proceed=s.next().charAt(0);
            dataoutput.writeChar(proceed);

        }while(proceed == 'y' || proceed == 'Y');
        try
        {
            datainput.close();
            dataoutput.close();
            objectInputStream.close();
            objectOutputStream.close();
            sk.close();
            System.out.println("Client Closing connection");
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

    }
}