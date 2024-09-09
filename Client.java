/*
 * Course : SENG4500 (Network and Distributed Computing)
 * Programmer : Avishek
 * Last Modified : 9/9/2024
 * 
 * This is the client program. It calls the server functions through the socket, 
 * which are requested by the user through the cli.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String hostName = "";
        int portNumber = 0;
        try {
            //configurable hostname and port number through command line parameter
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Incorrect syntax! \nTo run the program : java Client.java <hostname> <port number>");
        }
        

        try (
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ){
            System.out.println(in.readLine());
            // main method to run the client cli
            run(out, in, stdIn, scanner);
        } catch (UnknownHostException e){ // incorrect hostname specified; or server not running
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e){ // IO connection failure
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

    // method to run the respective function calls according to the user's choice
    public static void run(PrintWriter out, BufferedReader in, BufferedReader stdIn, Scanner scanner) throws IOException{
        int choice = 0;
        while (choice != -1) { 
            choice = printOptions(scanner);
            switch (choice) {
                case 1 -> // calls method to ask user to enter a country and print the capital city of that country, which is retrieved from the server
                    printCapital(out, in, stdIn, scanner);
                case 2 -> // calls a method to ask user to enter a country name and prints the estimated population retrieved from the server
                    printPopulation(out, in, stdIn, scanner);
                case 3 -> // calls a method to ask user to enter country name and capital city; and adds it to the servers memory
                    addCountryCapitalPair(out, in, stdIn, scanner);
                case -1 -> {
                    out.println("-1");
                    System.out.println("Connection terminated successfully.");
                    System.exit(1);
                }
                default -> {System.out.println("Selection out of range!"); continue;}
            }
        }
    }

    // method to print the options in the cli interface, and retrieve user's choice
    public static int  printOptions(Scanner scanner) throws IOException{
        System.out.println("*".repeat(50));
        System.out.println("1 : Find capital city of country");
        System.out.println("2 : Find population of country");
        System.out.println("3 : Add new country, capital city pair");
        System.out.println("-1 : Exit");
        System.out.print("Enter your choice : ");
        String choice = scanner.nextLine();
        // handling exception where user does not enter an integer
        try {
            int value = Integer.parseInt(choice);
        } catch (NumberFormatException e){
            System.out.println("The input must be an integer specified above");
            System.out.println("*".repeat(50));
            return 0;
        }
        System.out.println("*".repeat(50));
        return Integer.parseInt(choice);
    }

    // method to ask server for the capital of a user specified country, if present in the server memory
    public static void printCapital(PrintWriter out, BufferedReader in, BufferedReader stdIn, Scanner scanner) throws IOException{
        out.println("1");
        System.out.print("Enter name of country to find its capital : ");
        String country = scanner.nextLine();
        out.println(country);
        String serverResponse = in.readLine();
        // country infomation not present in server memory
        if (serverResponse.equals("403")){
            System.out.println("Data for given country does not exist!");
        }
        // country information present in server memory
        else{
            System.out.println("Capital of " + country + " : " + serverResponse);
        }
    }

    // method to ask server to estimate the population of a user specified country, if present in the server memory
    public static void printPopulation(PrintWriter out, BufferedReader in, BufferedReader stdIn, Scanner scanner) throws IOException{
        out.println("2");
        System.out.print("Enter name of country to find its population : ");
        String country = scanner.nextLine();
        out.println(country);
        String serverResponse = in.readLine();
        // country information not present in server memory; and hence population estimation not possible
        if (serverResponse.equals("403")){
            System.out.println("Data for given country does not exist!");
        }
        // successfully simulated population estimation
        else{
            System.out.println("Population of " + country + " is estimated to be : " + serverResponse);
        }
    }

    // method to add country, capital pair on the server memory
    public static void addCountryCapitalPair(PrintWriter out, BufferedReader in, BufferedReader stdIn, Scanner scanner) throws IOException{
        out.println("3");
        System.out.print("Enter name of country to add : ");
        String country = scanner.nextLine();
        System.out.print("Enter " + country + "'s capital : ");
        String capital = scanner.nextLine();
        // concatenating country and capital with a comma in the middle, so that one transmission is adequate
        out.println(country + "," + capital);
        String serverResponse = in.readLine();
        // successfully added pair to the server memory
        if ("201".equals(serverResponse)){
            System.out.println("Successfully added country, capital pair to server memory!");
        }
        // unable to add pair to the server memory
        else{
            System.out.println("Country, capital pair already exists in the memory!");
        }
    }
}