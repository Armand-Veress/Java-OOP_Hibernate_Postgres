package Client.Meniuri;

import Anexe.Tabele.*;
import Anexe.UnexpectedException;
import Anexe.Utils;
import Client.Client;
import Client.Operatii.OperatiiMedic;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@SuppressWarnings({"unchecked", "SpellCheckingInspection"})
public class MeniuMedic {
    public static void optiuni(){
        System.out.println("1. Consult nou");
        System.out.println("2. Pacienti");
        System.out.println("3. Consultari");
        System.out.println("4. Afisare program");
        System.out.println("5. Resetare pin");
        System.out.println("?. Optiuni");
        System.out.println("0. Log out");
    }
    public static void meniu(Client client, Scanner sc) throws IOException, ClassNotFoundException {
        fetching(client);
        System.out.println(" [Medic-Id: " + client.getSelf() + "]");

        optiuni();
        System.out.print("----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8----+----9----+\n>> ");
        while(true){
            String line = sc.next().trim();
            try{
                switch(line){
                    case "1":
                        OperatiiMedic.nouConsult(client, sc);
                        break;
                    case "2":
                        OperatiiMedic.listaPacienti(client, sc);
                        break;
                    case "3":
                        OperatiiMedic.listaConsultari(client, sc);
                        break;
                    case "4":
                        OperatiiMedic.afisareProgram(client, sc);
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
            } catch(Exception ex) {
                client.sendError(ex);
            }
            System.out.print("----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8----+----9----+\n>> ");
        }
    }

    public static void fetching(Client client) throws IOException, ClassNotFoundException {
        Serializable[] command = {"fetch", "Anexe.Tabele.Medic", client.getCont().getId()};
        Serializable response = client.sendMessage(command);
        Medic M = (Medic) response;
        client.setSelf(M);

        String query = "SELECT c FROM Consult c WHERE c.medic = :medic";
        HashMap<String, Object> params = new HashMap<>();
        params.put("medic", M);
        command = new Serializable[]{"select", query, "Anexe.Tabele.Consult", params};
        List<Consult> C = (List<Consult>) client.sendMessage(command);
        client.setConsultari(new HashSet<>(C));

        query = "SELECT DISTINCT p FROM Persoana p " +
                "LEFT JOIN FETCH p.boliCronice " +
                "WHERE p.cnp IN (SELECT DISTINCT c.persoana.cnp FROM Consult c WHERE c.medic = :medic)";
        command = new Serializable[]{"select", query, "Anexe.Tabele.Persoana", params};
        List<Persoana> P = (List<Persoana>) client.sendMessage(command);
        client.setPacienti(new HashSet<>(P));

        query = "SELECT b FROM BoliCronice b";
        command = new Serializable[]{"select", query, "Anexe.Tabele.BoliCronice", null};
        List<BoliCronice> B = (List<BoliCronice>) client.sendMessage(command);
        client.setBoliCronice(new HashSet<>(B));

        query = "SELECT p from Program p WHERE p.medic = :medic AND p.ziCalendaristica >= :now";
        params = new HashMap<>();
        params.put("medic", M);
        params.put("now", LocalDate.now());
        command = new Serializable[]{"select", query, "Anexe.Tabele.Program", params};
        List<Program> Pr = (List<Program>) client.sendMessage(command);
        client.setProgram(new HashSet<>(Pr));
    }
}
