import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

public class Server{
    private static HashMap<String, String> capitals = new HashMap<String, String>();

    public static void main(String[] args) {
        // change this so its user configurable
        int portNumber = 8080;
        populateCapitals();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)){

            while (true) {
                try (
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ){
                    // do something here
                    out.println("lesgo");
                    runFunctions(out, in);
                } catch (IOException e){
                    System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                }
            }
        } catch (IOException e){
            System.err.println("Could not listen on port " + portNumber);
        }
    }

    public static void runFunctions(PrintWriter out, BufferedReader in) throws IOException{
        String functionNumber = "";
        while (true) { 
            functionNumber = in.readLine();
            switch (functionNumber) {
                case "1":
                    String country = in.readLine();
                    String capital = getCapital(country);
                    out.println(capital);
                    break;
                case "2":
                    out.println(getPopulation(in.readLine()));
                    break;
                case "3":
                    addCapital();
                    break;
                default:
                System.out.println(functionNumber);
                    throw new AssertionError();
            }
        }
    }

    public static String getCapital(String country){
        String capital = capitals.get(country);
        return capital;
    }

    public static int getPopulation(String country){
        Random random = new Random();
        return (random.nextInt(9) + 1) * 3393440;
    }

    public static void addCapital(){

    }

    public static void populateCapitals(){
        String filename = "countries_capitals.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String pair;
            while ((pair = reader.readLine()) != null) {
                String[] pairArray = pair.split(",");
                capitals.put(pairArray[0], pairArray[1]);
            }
        } catch (IOException e){
            System.out.println("Couldn't read file " + filename);
        }
    }

}