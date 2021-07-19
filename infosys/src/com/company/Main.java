package com.company;

import jdk.swing.interop.SwingInterOpUtils;

import java.util.Scanner;
import java.util.jar.JarOutputStream;

public class Main {


    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String instr;
        instr=sc.nextLine();

        char[] output =instr.toCharArray();


        for (int i = 0; i <output.length ; i++) {
            if(output[i] == 'a' || output[i] == 'e' ||output[i] == 'i'||output[i] == 'o'||output[i] == 'u'){

                 int res=i*5;
                 int oddSum=oddSumtillN(res);
                 while(oddSum > 9){
                     System.out.println(oddSum);
                     oddSum = sumOfDigits(oddSum);
                 }
                System.out.println(oddSum);
                output[i]=String.valueOf(oddSum).charAt(0);
            }
        }


        instr=String.valueOf(output);
        System.out.println(instr);
    }

    private static int oddSumtillN(int n) {
        int sum=0;
        for (int i = 1; i <=n; i++) {
             if(i%2 != 0){
                 sum=sum+i;
             }
        }
        return  sum;
    }

    private static int sumOfDigits(int n) {
        int sum=0;
        while (n>0) {
             sum=sum+n%10;
            n=n/10;
        }
        return sum;
    }


}
