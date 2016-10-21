package coplan;

import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {
   static InputStreamReader isReader;
   static BufferedReader bufReader;

   public enum Subsystem {
      USER, COURSE, OTHER
   }

   public static void main(String[] args ) {
      System.out.println("Hi, I'm the Java App!");
      Router router = new Router();
      router.listen();
   }

   private static class Router {

      public Router() { }

      public void listen() {
         try {
            bufReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
               String command = null;
               if ((command = bufReader.readLine()) != null) {
                  route(command);
                  System.out.println("Processing the command, " + command);
               }
            }
         } catch (IOException e) {
            e.printStackTrace();
         }

      }

      public void route(String cmd) {
         String[] command = cmd.split(" ");
         // Subsystem the command is intended for
         String subsystem = command[0];
         System.out.println(command[0]);
         if (subsystem.equals("user")) {
            System.out.println("Do something with the user subsystem.");
            System.out.println("Command to do: " + cmd);
         }
         else if (subsystem.equals("course")) {
            System.out.println("Do something with the course subsystem.");
            System.out.println("Command to do: " + cmd);
         }
         else if (subsystem.equals("other")) {
            System.out.println("Do something with some other subsystem.");
            System.out.println("Command to do: " + cmd);
         }
         else {
            System.out.println("Unrecognized command...");
         }
      }
   }
}
