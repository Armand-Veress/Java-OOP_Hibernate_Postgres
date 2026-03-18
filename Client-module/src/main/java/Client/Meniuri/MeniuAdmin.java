package Client.Meniuri;

import Anexe.Tabele.*;
import Anexe.UnexpectedException;
import Anexe.Utils;
import Client.Client;
import Client.Operatii.OperatiiAdmin;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings({"SpellCheckingInspection", "unchecked"})
public class MeniuAdmin {
    public static void optiuni(){
        System.out.println("1. Adauga admin");
        System.out.println("2. Adauga medic");
        System.out.println("3. Solicitari");
        System.out.println("4. Program");
        System.out.println("5. Resetare pin");
        System.out.println("?. Optiuni");
        System.out.println("0. Log out");
    }
    public static void meniu(Client client, Scanner sc) throws IOException, ClassNotFoundException {
        fetching(client);
        System.out.println(" [Admin-Id: " + client.getSelf() + "]");

        optiuni();
        System.out.print("----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8----+----9----+\n>> ");
        while(true){
            String line = sc.next().trim();
            try{
                switch(line){
                    case "1":
                        OperatiiAdmin.newAdmin(client, sc);
                        break;
                    case "2":
                        OperatiiAdmin.newMedic(client, sc);
                        break;
                    case "3":
                        OperatiiAdmin.tratareSolicitari(client, sc);
                        break;
                    case "4":
                        OperatiiAdmin.program(client, sc);
                        break;
                    case "5":
                        Utils.resetarePin(client, sc);
                        break;
                    case "0":
                        return;
                    case "?":
                        optiuni();
                        break;
                    case "":
                        break;
                    default:
                        System.out.println("Optiune invalida.\n?. Optiuni");
                        break;
                }
            } catch(UnexpectedException ex) {
                System.out.println(ex.getMessage());
            } catch(Exception ex){
                client.sendError(ex);
            }
            System.out.print("----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8----+----9----+\n>> ");
        }
    }

    public static void fetching(Client client) throws IOException, ClassNotFoundException {
        Serializable[] command = {"fetch", "Anexe.Tabele.Admin", client.getCont().getId()};
        Serializable response = client.sendMessage(command);
        Admin A = (Admin) response;
        client.setSelf(A);

        String query = "SELECT c FROM Cont c WHERE c.isActiv = false";
        command = new Serializable[]{"select", query, "Anexe.Tabele.Cont", null};
        List<Cont> C = (List<Cont>) client.sendMessage(command);
        client.setConturi(new HashSet<>(C));
    }
}
