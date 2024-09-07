
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

        // the following should be configurable
        String hostName = "localhost";
        int portNumber = 8080;

        try (
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            
        ){
            //do something here
            System.out.println(in.readLine());
            run(out, in, stdIn, scanner);
        } catch (UnknownHostException e){
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e){
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

    public static void run(PrintWriter out, BufferedReader in, BufferedReader stdIn, Scanner scanner) throws IOException{
        int choice = 0;
        while (choice != -1) { 
            choice = printOptions(scanner);
            switch (choice) {
                case 1:
                    // calls method to ask user to enter a country and print the capital city of that country, which is retrieved from the server
                    printCapital(out, in, stdIn, scanner);
                    break;
                case 2: 
                    // calls a method to ask user to enter a country name and prints the estimated population retrieved from the server
                    printPopulation(out, in, stdIn, scanner);
                    break;
                case 3:
                    System.out.println("3");
                    break;
                case -1: 
                    System.out.println("-1");
                    break;
                default:
                    System.out.println("Selection out of range!");
            }
        }
    }

    public static int  printOptions(Scanner scanner){
        System.out.println("*".repeat(50));
        System.out.println("1 : Find capital city of country");
        System.out.println("2 : Find population of country");
        System.out.println("3 : Add new country, capital city pair");
        System.out.println("-1 : Exit");
        System.out.print("Enter your choice : ");
        int choice = scanner.nextInt();
        System.out.println("*".repeat(50));
        return choice;
    }

    public static void printCapital(PrintWriter out, BufferedReader in, BufferedReader stdIn, Scanner scanner) throws IOException{
        out.println("1");
        System.out.print("Enter name of country to find its capital : ");
        scanner.nextLine();
        String country = scanner.nextLine();
        out.println(country);
        String serverResponse = in.readLine();
        System.out.println("Capital of " + country + " : " + serverResponse);
    }

    public static void printPopulation(PrintWriter out, BufferedReader in, BufferedReader stdIn, Scanner scanner) throws IOException{
        out.println("2");
        System.out.print("Enter name of country to find its population : ");
        scanner.nextLine();
        String country = scanner.nextLine();
        out.println(country);
        System.out.println("Popultion of " + country + "is estimated to be : " + in.readLine());
    }
}