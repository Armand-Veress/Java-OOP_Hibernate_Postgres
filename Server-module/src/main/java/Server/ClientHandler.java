package Server;

import Server.DataBase.Database;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void run() {
        ObjectOutputStream out;
        ObjectInputStream in;
        try{
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            Serializable input;
            while ((input = (Serializable) in.readObject()) != null) {
                if(input instanceof Exception) {
                    System.out.println("Terminal error ( " + clientSocket + " ): ");
                    ((Exception) input).printStackTrace();
                }
                else
                    out.writeObject(Database.commandInterpreter(input));
            }
        } catch(Exception ex){
            System.out.println("Error communicating with client: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " - " + ex.getMessage());
        }
        finally {
            try{
                if(clientSocket != null && !clientSocket.isClosed()){
                    clientSocket.close();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}

