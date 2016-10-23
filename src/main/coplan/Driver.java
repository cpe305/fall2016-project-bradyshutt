package com.bshutt.coplan;

import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {


   public static void main(String[] args ) {

      int numCommands = 0;

      System.out.println("Hi, I'm the Java App!");
      InputStreamReader isReader;
      BufferedReader bufReader;
      String next = "";

      try {
         isReader = new InputStreamReader(System.in);
         bufReader = new BufferedReader(isReader);

         //while ((next = scan.nextLine()) != null && numCommands < 4) {
         while (numCommands < 4) {
            String inputStr = null;

            if ((inputStr = bufReader.readLine()) != null) {
               System.out.println("Processing the command, " + inputStr);

               numCommands++;
            }
            else {
               System.err.println("aint doin' nuthin!");
            }
         }
      } catch (IOException e) {
         System.out.println("Crap i died");
         e.printStackTrace();
      }



   }
}
