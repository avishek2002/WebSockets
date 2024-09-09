/*
 * Course : SENG4500 (Network and Distributed Computing)
 * Programmer : Avishek
 * Last Modified : 9/9/2024
 * 
 * This is the server program. It stores country and capital pairs in a hashmap, 
 * and performs functions requested by the client through the socket.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Server{
    // csv data stored in a hashmap (needs to be populated from the main method), inaccessible from outside this class
    private static final HashMap<String, String> capitals = new HashMap<String, String>();

    public static void main(String[] args) {
        // user configurable tcp port
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the TCP port for the socket to listen on : ");
        int portNumber = scanner.nextInt();
        populateCapitals();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)){
            while (true) {
                try (
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ){
                    // main method for the server to call the client indicated function
                    runFunctions(out, in);
                } catch (IOException e){
                    System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                }
            }
        } catch (IOException e){
            System.err.println("Could not listen on port " + portNumber);
        }
    }

    // method to run methods requested from the client cli
    public static void runFunctions(PrintWriter out, BufferedReader in) throws IOException{
        String functionNumber = "";
        while (true) { 
            functionNumber = in.readLine();
            switch (functionNumber) {
                case "1" -> out.println(getCapital(in.readLine()));
                case "2" -> out.println(getPopulation(in.readLine()));
                case "3" -> out.println(addCapital(in.readLine()));
                default -> {
                    System.out.println(functionNumber);
                    throw new AssertionError();
                }
            }
        }
    }

    // method to retrieve the capital city of a user specified country
    public static String getCapital(String country){
        // retrieve capital from the hashmap (note: key is stored in lower case for consistency purposes)
        String capital = capitals.get(country.toLowerCase());
        if (capital == null){
            capital = "403";
        }
        return capital;
    }

    // method to calculate the population of a user specified country, the population is not accurate
    public static int getPopulation(String country){
        // if country is not in our memory, we don't simulate the population estimation
        if (!(capitals.containsKey(country.toLowerCase()))){
            return 403; // forbidden request code
        }
        Random random = new Random();
        return (random.nextInt(9) + 1) * 3393440;
    }

    // method to add new country and capital pair to the memory (ie the hashmap)
    public static int addCapital(String pair){
        // split string to [country, capital] pair
        String[] pairArray = pair.split(",");
        // if the pair is  already in the memory
        if ((capitals.containsKey(pairArray[0].toLowerCase()))){
            return 403; // forbidden request; already exists
        }
        capitals.put(pairArray[0].toLowerCase(), pairArray[1]);
        return 201; // successfully added resource
    }

    // method to populate the hashmap storing country, capital pair
    // make sure csv file is in the same directory as this program
    public static void populateCapitals(){
        String filename = "countries_capitals.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String pair;
            while ((pair = reader.readLine()) != null) {
                // split string to country, capital pair using String.split(regex)
                String[] pairArray = pair.split(",");
                // changing country value to lower case for consistency purposes
                capitals.put(pairArray[0].toLowerCase(), pairArray[1]);
            }
        } catch (IOException e){
            System.out.println("Couldn't read file " + filename);
        }
    }

}