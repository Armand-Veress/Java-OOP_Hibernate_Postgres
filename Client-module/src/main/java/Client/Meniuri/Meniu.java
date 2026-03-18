package Client.Meniuri;

import Anexe.UnexpectedException;
import Anexe.Utils;
import Client.Client;
import Client.Operatii.OperatiiPersoana;

import java.io.IOException;
import java.util.Scanner;

@SuppressWarnings("SpellCheckingInspection")
public class Meniu {
    public static void optiuni() {
        System.out.println("1. Programare");
        System.out.println("2. Log in");
        System.out.println("3. Solicita cont");
        System.out.println("?. Optiuni");
        System.out.println("0. Inchide sesiunea");
    }

    public static void meniu(Client client, Scanner sc) throws IOException {
        optiuni();
        System.out.print("----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8----+----9----+\n>> ");
        while(true){
            String line = sc.next().trim();
            try{
                switch(line){
                    case "1":
                        OperatiiPersoana.programare(client, sc);
                        break;
                    case "2":
                        Utils.logIn(client, sc);
                        if(client.getSelf() != null) {
                            client.logOut();
                            optiuni();
                        }
                        break;
                    case "3":
                        OperatiiPersoana.solicitaCont(client, sc);
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
}
