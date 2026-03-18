package Client;

import Client.Meniuri.Meniu;
import java.util.Scanner;

public class $Terminal {
    public static void main(String[] args) {
        try{
            Client client = new Client();
            client.startConnection("127.0.0.1", 8080);
            System.out.println("Connected to server.");

            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\n");

            Meniu.meniu(client, sc);
            client.stopConnection();
        }
        catch(Exception ex){
            System.out.println("Error in communicating with the server.");
        }
    }
}
