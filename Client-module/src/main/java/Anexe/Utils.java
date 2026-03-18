package Anexe;

import java.util.*;
import Anexe.Tabele.*;
import java.io.Serializable;
import java.time.LocalDate;

import Client.Client;
import Client.Meniuri.MeniuAdmin;
import Client.Meniuri.MeniuMedic;
import Client.Meniuri.MeniuPersoana;
import org.mindrot.jbcrypt.BCrypt;

@SuppressWarnings({"SpellCheckingInspection", "unchecked"})
public class Utils {
    public static void resetarePin(Client client, Scanner sc) throws Exception {
        System.out.print("Pinul curent: ");
        String pin = sc.next().trim();
        if(!isPin(pin)){
            System.out.println("Pin invalid!");
            return;
        }
        if(!Utils.checkPin(pin, client.getCont().getPin())){
            System.out.println("Pin invalid!");
            return;
        }
        System.out.print("Introduceti noul pin: ");
        pin = sc.next().trim();
        while(!Utils.isPin(pin)){
            System.out.println("Pin invalid. (ex. 1234) - 4 cifre (0->9) ");
            System.out.print("Introduceti noul pin: ");
            pin = sc.next().trim();
        }
        client.getCont().setPin(Utils.hashPin(pin));
        Serializable[] command = new Serializable[]{"merge", client.getCont()};
        Serializable response = client.sendMessage(command);
        if(response.equals("fail")){
            System.out.println("Eroare neasteptata!. Operatie esuata.");
        }
    }
    //** Utilitati Database **
    public static String getTabela(String rol){
        return switch (rol) {
            case "admin" -> "Anexe.Tabele.Admin";
            case "medic" -> "Anexe.Tabele.Medic";
            case "persoana" -> "Anexe.Tabele.Persoana";
            default -> null;
        };
    }

    //** Utilitati tabel Persoane **
    public static char sex(String cnp){
        if(Integer.parseInt(cnp.substring(0, 1)) % 2 == 1)
            return 'm';
        return 'f';
    }
    public static LocalDate dataNastere(String cnp){
        int an = Integer.parseInt(cnp.substring(1, 3));
        if(Integer.parseInt(cnp.substring(0, 1)) < 5)
            an += 1900;
        else an += 2000;
        int luna = Integer.parseInt(cnp.substring(3, 5));
        int zi = Integer.parseInt(cnp.substring(5, 7));
        return LocalDate.of(an, luna, zi);
    }

    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "UnusedAssignment"})
    public static boolean isCnp(String cnp){
        if(cnp.length() != 13)
            return false;
        int d = Character.digit(cnp.charAt(0), 10);
        if(d < 1 || d > 6) return false;
        try{
            Utils.dataNastere(cnp);
            d = Integer.parseInt(cnp.substring(7, 9));
            if(d == 0 || (d > 52 && d != 70)) return false;
            d = Integer.parseInt(cnp.substring(9));
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    //---------------------------------------------------------------------------------------------
    //** jBCrypt hashing **
    public static String hashPin(String pin){
        return BCrypt.hashpw(pin, BCrypt.gensalt());
    }

    public static boolean checkPin(String pin, String hash){
        return BCrypt.checkpw(pin, hash);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isPin(String pin){
        if(pin.length() != 4)
            return false;
        for(int i=0; i < pin.length(); i++){
            if(Character.digit(pin.charAt(i), 10) == -1)
                return false;
        }
        return true;
    }
    //----------------------------------------------------------------------------------------------
    public static HashSet<LocalDate> getZileLibere(int an){
        HashSet<LocalDate> L = new HashSet<>();
        L.add(LocalDate.of(an, 1, 1));
        L.add(LocalDate.of(an, 1, 2));
        L.add(LocalDate.of(an, 1, 6));
        L.add(LocalDate.of(an, 1, 7));
        L.add(LocalDate.of(an, 1, 24));
        L.add(LocalDate.of(an, 4, 18));
        L.add(LocalDate.of(an, 4, 20));
        L.add(LocalDate.of(an, 5, 1));
        L.add(LocalDate.of(an, 5, 2));
        L.add(LocalDate.of(an, 6, 1));
        L.add(LocalDate.of(an, 6, 8));
        L.add(LocalDate.of(an, 6, 9));
        L.add(LocalDate.of(an, 8, 15));
        L.add(LocalDate.of(an, 11, 30));
        L.add(LocalDate.of(an, 12, 1));
        L.add(LocalDate.of(an, 12, 25));
        L.add(LocalDate.of(an, 12, 26));
        return L;
    }

    //** Optiuni Meniu.meniu **
    public static void logIn(Client client, Scanner sc) throws Exception {
        System.out.print("Id: ");
        String id = sc.next().trim();
        if(id.isEmpty()) return;
        System.out.print("Password: ");
        String pin = sc.next().trim();
        if(!isPin(pin)) {
            System.out.println("Login error: Id sau Pin gresit.");
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        String query = "SELECT c FROM Cont c WHERE c.id = :id";
        Serializable[] command = {"select", query, "Anexe.Tabele.Cont", params};
        Serializable response = client.sendMessage(command);

        if(response.equals("fail")) {
            System.out.println("Eroare neasteptata! Operatie esuata.");
            return;
        }
        List<Cont> rez = (List<Cont>) response;
        if(rez.isEmpty()){
            System.out.println("Login error: Id sau Pin gresit.");
            return;
        }

        for(Cont cont : rez){
            String hash = cont.getPin();
            if(Utils.checkPin(pin, hash)){
                if(!cont.getIsActiv()){
                    System.out.println("Solicitarea dvs. este in curs de procesare. Incercati mai tarziu.");
                    return;
                }
                client.setCont(cont);
                switch(cont.getRol()){
                    case "admin":
                        MeniuAdmin.meniu(client, sc);
                        return;
                    case "medic":
                        MeniuMedic.meniu(client, sc);
                        return;
                    case "persoana":
                        MeniuPersoana.meniu(client, sc);
                        return;
                    default:
                        System.out.println("Eroare neasteptata! Operatie esuata.");
                        return;
                }
            }
        }
        System.out.println("Login error: Id sau Pin gresit.");
    }
}