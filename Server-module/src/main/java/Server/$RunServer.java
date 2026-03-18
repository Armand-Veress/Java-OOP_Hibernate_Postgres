package Server;

import java.io.IOException;
import Server.DataBase.$PopulareDB;

public class $RunServer {
    public static void main(String[] args) throws IOException {
        try {
            System.out.println("Verificare/Populare bază de date...");
            $PopulareDB.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Baza de date este deja populată sau a apărut o eroare: " + e.getMessage());
        }
        Server server = new Server();
        server.run(8080);
        server.close();
    }
}

