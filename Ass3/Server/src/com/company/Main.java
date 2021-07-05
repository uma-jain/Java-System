package com.company;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

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


public class Main {
    private static Statement stmt;
    private static ResultSet rs;

    public static void main(String[] args) throws IOException, SQLException {
        DataInputStream datainput=null;
        DataOutputStream dataoutput=null;

        ObjectInputStream  objinput=null;
        BufferedReader br=null;

        int del_or_edit,item_no,inc_or_dec;
        //link socket
        System.out.println("SERVER STARTED");
        System.out.println("waiting for client to connect ");
        ServerSocket soc = new ServerSocket(7899);

        while (true)
        {
            Socket sk= null;

            try
            {
                // socket object to receive incoming client requests
                sk = soc.accept();
                System.out.println("A new client is connected : " + sk);

                datainput  = new DataInputStream(sk.getInputStream()); // get ip from connected socket
                dataoutput    = new DataOutputStream(sk.getOutputStream()); //give output to connected socket
                //   br=new BufferedReader(new InputStreamReader(System.in));

                OutputStream outputStream = sk.getOutputStream();
                // create an object output stream from the output stream so we can send an object through it
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

                // get the input stream from the connected socket
                InputStream inputStream = sk.getInputStream();
                // create a DataInputStream so we can read data from it.
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(sk,datainput,dataoutput,objectInputStream,objectOutputStream);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                sk.close();
                e.printStackTrace();
            }
        }

    }
}
class ClientHandler extends Thread  {

    // write your code here
    Scanner s = new Scanner(System.in);
    char proceed;
    MainClass a=new MainClass();
    int choice;
    final Socket sk;
    DataInputStream datainput=null;
    DataOutputStream dataoutput=null;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    //declare database
    DB d=new DB();
    Connection con=null;
    private  static  Statement stmt;
    private  static  ResultSet rs;

    //choice will be from client
    public ClientHandler(Socket sk, DataInputStream datainput, DataOutputStream dataoutput, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.sk=sk;
        this.datainput=datainput;
        this.dataoutput=dataoutput;
        this.objectInputStream=inputStream;
        this.objectOutputStream=outputStream;
    }

    public  void  run(){
        while(true){
            try{

                do{
                    choice=datainput.readInt();
                    System.out.println("Choice from client is"+choice);
                    switch(choice) {
                        case 1: {
                            //get product and fill object
                            con = d.getConnection();
                            stmt = con.createStatement();
                            rs = stmt.executeQuery("select * from \n" +
                                    "                    product");
                            a.empty_productList();
                            while (rs.next()) {
                                a.addToProductList(new productclass(rs.getInt(1),
                                        rs.getString(2),
                                        rs.getInt(3),
                                        rs.getInt(4),
                                        rs.getString(5),
                                        rs.getString(6)
                                ));

                            }

                            d.closeConnection();
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(a);
                        }
                        break;
                        case 2: {
                            con = d.getConnection();
                            stmt = con.createStatement();
                            rs = stmt.executeQuery("select * from \n" +
                                    "                    product");
                            a.empty_productList();
                            while (rs.next()) {
                                a.addToProductList(new productclass(
                                        rs.getInt(1),
                                        rs.getString(2),
                                        rs.getInt(3),
                                        rs.getInt(4),
                                        rs.getString(5),
                                        rs.getString(6)
                                ));
                            }
                            int a1;

                            d.closeConnection();
                            objectOutputStream.reset();
                            a.check();
                            objectOutputStream.writeObject(a);
                        }
                        break;
                        case 3: {
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(a);
                            //get item id and quantity
                            //  and add it to cart and decrease quantity
                            //give success / failure alert
                            int id, qty;
                            id = datainput.readInt();
                            qty = datainput.readInt();
                            con = d.getConnection();
                            stmt = con.createStatement();
                            String sql = "INSERT INTO cart(" + "item_id," + "product_name," + "price," + "quantity," + "total_price)" + "VALUES(?,?,?,?,?)";
                            PreparedStatement pstmt = con.prepareStatement(sql);
                            rs = stmt.executeQuery("select quantity  from product  WHERE item_id  = "+id);
                            int added = 0;
                            while (rs.next()) {
                                added = rs.getInt(1);
                            }
                            if (added > qty) {
                                //send confirmation
                                //add the row from product which has id id to cart and cart table;
                                rs = stmt.executeQuery("select *  from product " + " WHERE item_id=" + id);

                                while (rs.next()) {
                                    pstmt.setInt(1, rs.getInt(1));
                                    pstmt.setString(2, rs.getString(2));
                                    pstmt.setInt(3, rs.getInt(3));
                                    pstmt.setInt(4, qty);
                                    pstmt.setInt(5, rs.getInt(3) * qty);
                                    pstmt.executeUpdate();
                                }
                                //dec quantity in products table
                                String sql1 = "UPDATE product SET quantity = ? WHERE item_id = ?";
                                PreparedStatement ps = con.prepareStatement(sql1);
                                ps.setInt(1, added - qty);
                                ps.setInt(2, id);
                                ps.executeUpdate();
                                d.closeConnection();

                                System.out.println("item Added successfully");
                                dataoutput.flush();
                                dataoutput.writeUTF("Item Added successfully");

                            } else {
                                //send failure
                                d.closeConnection();
                                dataoutput.writeUTF("Less availability than required,sorry!");
                            }


                        }
                        break;
                        case 4: {
                            //set data in cart
                            con = d.getConnection();
                            stmt = con.createStatement();
                            rs = stmt.executeQuery("select * from \n" + "cart");
                            a.empty_cart();
                            while (rs.next()) {
                                a.addToCart(new checkoutclass(
                                        rs.getInt(1),
                                        rs.getString(2),
                                        rs.getInt(3),
                                        rs.getInt(4),
                                        rs.getInt(5)
                                ));

                            }

                            objectOutputStream.reset();
                            objectOutputStream.writeObject(a);
                            //will display proceed or edit option in client
                            //if edit
                            //show delete or edit quantity options
                            //get it
                   /* int choice_for_edit_procees=datainput.readInt();
                    if(choice_for_edit_procees == 2) {
                        //get choice = delete || edit from client
                        del_or_edit = datainput.readInt();
                        System.out.println("del or edit choice is" + del_or_edit);
                        switch (del_or_edit) {
                            case 1:
                                //get item no to delete;
                                item_no = datainput.readInt();
                                System.out.println("delete item with no " + item_no);
                                //call function in data class to delete item from cart
                                rs = stmt.executeQuery("select quantity  from product " + " WHERE item_id=" + item_no);
                                //quantity in products
                                int added = 0,to_dec=0;
                                while (rs.next()) {
                                    added = rs.getInt(1);
                                }
                                //inc quantity in main table;
                                //inc quantity in products table
                                String sql1 = "UPDATE product SET quantity = ? WHERE item_id = ?";
                                PreparedStatement ps = con.prepareStatement(sql1);
                                ps.setInt(1, added+to_dec);
                                ps.setInt(2,item_no);
                                ps.executeUpdate();
                                //then deleet
                                int re=stmt.executeUpdate("DELETE FROM cart " +"WHERE item_id ="+item_no);

                                break;
                            case 2:
                                //get item no and  inc or dec
                                item_no = datainput.readInt();
                                inc_or_dec = datainput.readInt();
                                System.out.println("seletcted opt fro ic and dec are" + inc_or_dec);
                                //switch inc or dec
                                switch (inc_or_dec) {
                                    case 1:
                                        //update cart ,inc quantity by 1
                                        //get existing quantity;

                                        rs = stmt.executeQuery("select quantity from cart " + " WHERE item_id=" + item_no);
                                        int qty_inc = 0;
                                        while (rs.next()) {
                                            qty_inc = rs.getInt(1);
                                        }
                                        ++qty_inc;
                                        String sql = "UPDATE cart SET quantity = ? WHERE item_id = ?";
                                        ps = con.prepareStatement(sql);
                                        ps.setInt(1, qty_inc);
                                        ps.setInt(2, item_no);
                                        ps.executeUpdate();
                                        //now again update product by decrementing its quantity
                                        sql = "UPDATE product SET quantity =quantity -? WHERE item_id = ?";
                                        ps = con.prepareStatement(sql);
                                        ps.setInt(1, 1);
                                        ps.setInt(2, item_no);
                                        ps.executeUpdate();
                                        break;
                                    case 2:
                                        //update cart,dec quantity by 1
                                        rs = stmt.executeQuery("select quantity from cart " + " WHERE item_id=" + item_no);
                                        int qty_dec = 0;
                                        while (rs.next()) {
                                            qty_dec = rs.getInt(1);
                                        }
                                        stmt=con.createStatement();
                                        sql1="UPDATE cart SET quantity = ? WHERE item_id = ?";
                                        PreparedStatement ps1 = con.prepareStatement(sql1);
                                        ps1.setInt(1, --qty_dec);
                                        ps1.setInt(2, item_no);
                                        ps1.executeUpdate();
                                        sql1 = "UPDATE product SET quantity =quantity+? WHERE item_id = ?";
                                        ps1 = con.prepareStatement(sql1);
                                        ps1.setInt(1, 1);
                                        ps1.setInt(2, item_no);
                                        ps1.executeUpdate();
                                        break;
                                }
                                //call function in data class to inc or dec item from cart


                        }
                    }*/
                            d.closeConnection();
                        }
                        break;
                        case 5:
                        {
                            con = d.getConnection();
                            stmt = con.createStatement();
                            rs = stmt.executeQuery("select * from \n" + "cart");
                            a.empty_cart();
                            while (rs.next()) {
                                a.addToCart(new checkoutclass(
                                        rs.getInt(1),
                                        rs.getString(2),
                                        rs.getInt(3),
                                        rs.getInt(4),
                                        rs.getInt(5)
                                ));

                            }
                            //now the session ends
                            //so check the quantities in cart and dec that no.from quantities
                            //in product table
                            //delete cart table
                            //empty cartlist
                            d.closeConnection();
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(a);
                        }
                        break;

                        //set a flag that if bill is paid or not ,if yes dont show do you want to continue

                    }
                    proceed=datainput.readChar();
                    System.out.println("continue result from client is"+proceed);

                }while(proceed == 'y' || proceed == 'Y');



            }catch(IOException | SQLException E){
                E.printStackTrace();
                try
                {
                    datainput.close();
                    dataoutput.close();
                    objectInputStream.close();
                    objectOutputStream.close();
                    sk.close();
                    System.out.println("Server Closing connection");
                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }

        }
    }




}