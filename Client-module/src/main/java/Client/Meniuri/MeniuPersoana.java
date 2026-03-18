package Client.Meniuri;

import Anexe.Tabele.*;
import Anexe.UnexpectedException;
import Anexe.Utils;
import Client.Client;
import Client.Operatii.OperatiiPersoana;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@SuppressWarnings({"SpellCheckingInspection", "unchecked"})
public class MeniuPersoana {
    public static void optiuni(){
        System.out.println("1. Istoric medical");
        System.out.println("2. Modifica detalii cont");
        System.out.println("3. Programare");
        System.out.println("?. Optiuni");
        System.out.println("0. Log out");
    }

    public static void meniu(Client client, Scanner sc) throws IOException, ClassNotFoundException {
        fetching(client);
        System.out.println(" [User-Id: " + client.getCont().getId() + "]");

        optiuni();
        System.out.print("----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8----+----9----+\n>> ");
        while(true){
            String line = sc.next().trim();
            try{
                switch(line){
                    case "1":
                        OperatiiPersoana.istoricMedical(client.getConsultari());
                        break;
                    case "2":
                        OperatiiPersoana.modificaDate(client, sc);
                        break;
                    case "3":
                        OperatiiPersoana.programari(client, sc);
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
        Serializable[] command = {"fetch", Utils.getTabela(client.getCont().getRol()), client.getCont().getId()};
        Serializable response = client.sendMessage(command);
        Persoana P = (Persoana) response;
        client.setSelf(P);

        String query = "SELECT c FROM Consult c WHERE c.persoana = :cnp";
        HashMap<String, Object> params = new HashMap<>();
        params.put("cnp", P);
        command = new Serializable[]{"select", query, "Anexe.Tabele.Consult", params};
        List<Consult> C = (List<Consult>) client.sendMessage(command);
        client.setConsultari(new HashSet<>(C));

        query = "SELECT p from Program p WHERE p.persoana = :persoana AND p.ziCalendaristica >= :now";
        params = new HashMap<>();
        params.put("persoana", P);
        params.put("now", LocalDate.now());
        command = new Serializable[]{"select", query, "Anexe.Tabele.Program", params};
        List<Program> Pr = (List<Program>) client.sendMessage(command);
        client.setProgram(new HashSet<>(Pr));
    }
}
