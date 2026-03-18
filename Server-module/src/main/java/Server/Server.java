package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    @SuppressWarnings({"InfiniteLoopStatement", "CallToPrintStackTrace"})
    public void run(int port){
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server has started and is waiting for connection...");

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch(Exception ex){
            System.out.println("Server error" + ex.getMessage());
        } finally {
            if(serverSocket != null && !serverSocket.isClosed()){
                try{
                    serverSocket.close();
                    System.out.println("Server has stopped.");
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public void close() throws IOException {
        if(serverSocket != null && !serverSocket.isClosed()){
            serverSocket.close();
        }
    }
}
