package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        try{

            File file=new File("C:\\Users\\Uma Jain\\Desktop\\projects\\JavaAss\\SEM6\\file_reader\\src\\com\\company\\code.txt");

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            StringBuffer sb=new StringBuffer();

            while((line = br.readLine()) != null){
                //process the line
                sb.append(line);
                sb.append("\n");
            }

        }catch (Exception e){

        }
    }
}
