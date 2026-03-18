package Client.Operatii;

import Anexe.Tabele.*;
import Anexe.UnexpectedException;
import Anexe.Utils;
import Client.Client;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import Anexe.ZiASaptamanii;
import java.util.*;

@SuppressWarnings({"unchecked", "SpellCheckingInspection"})
public class OperatiiPersoana {

    public static void solicitaCont(Client client, Scanner sc) throws Exception {
        System.out.print("CNP: ");
        String cnp = sc.next().trim();
        if(cnp.isEmpty()) return;
        if(!Utils.isCnp(cnp)){
            System.out.println("CNP invalid.");
            return;
        }

        Serializable[] command = {"fetch", "Anexe.Tabele.Persoana", cnp};
        Serializable response = client.sendMessage(command);

        if(response == null){
            System.out.println("Nu va regasiti printre clientii nostri.");
            System.out.println("Pentru a putea solicita un cont este nevoie sa va programati pentru un consult.");
            return;
        }

        UnexpectedException.checkResponse(response);

        HashMap<String, Object> params = new HashMap<>();
        params.put("cnp", cnp);
        String query = "SELECT c FROM Cont c WHERE c.id = :cnp";
        command = new Serializable[]{"select", query, "Anexe.Tabele.Cont", params};
        response = client.sendMessage(command);

        UnexpectedException.checkResponse(response);
        List<Cont> rez = (List<Cont>) response;
        if(!rez.isEmpty()){
            if(rez.getFirst().getIsActiv())
                System.out.println("Contul dvs. este activ. Incercati: Log in");
            else
                System.out.println("Aveti deja o solicitare in curs de procesare.");
            return;
        }

        System.out.print("Introduceti un pin format din 4 cifre: ");
        String pin = sc.next().trim();
        while(!Utils.isPin(pin)){
            System.out.println("Pin invalid! (ex. 1234) - 4 cifre (0->9) ");
            System.out.print("Introduceti un pin format din 4 cifre: ");
            pin = sc.next().trim();
        }
        Cont cont = new Cont("persoana", cnp, Utils.hashPin(pin));
        command = new Serializable[]{"add", cont};
        response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);
        System.out.println("Solicitarea dvs. este in curs de procesare.");
    }

    public static void istoricMedical(Set<Consult> consultari) {
        System.out.println(" -> Istoric medical: ");
        for(Consult c : consultari){
            System.out.println(c);
        }
    }

    public static void modificaDate(Client client, Scanner sc) throws Exception {
        System.out.println("1. Modifica detalii personale");
        System.out.println("2. Resetare pin");
        System.out.println("0. Anulare");
        Persoana P = (Persoana) client.getSelf();
        Serializable[] command;
loop:   while(true){
            System.out.print(">> ");
            String line = sc.next().trim();
            switch(line){
                case "1":
                    System.out.print("(Alergii: " + P.getAlergii() + ")\n" + "Alergii: ");
                    line = sc.next().trim();
                    if(!line.isEmpty()) {
                        P.setAlergii(line);
                    }
                    System.out.print("(Detalii contact: " + P.getContact() + ")\n" + "Contact: ");
                    line = sc.next().trim();
                    if(!line.isEmpty()) {
                        P.setContact(line);
                    }
                    command = new Serializable[]{"merge", P};
                    break loop;
                case "2":
                    Utils.resetarePin(client, sc);
                    command = new Serializable[]{"merge", client.getCont()};
                    break loop;
                case "0":
                    return;
                default:
                    System.out.println("Comanda invalida.");
                    break;
            }
        }
        Serializable response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);
    }

    public static void programari(Client client, Scanner sc) throws Exception {
        System.out.println("1. Programari");
        System.out.print("2. Programare noua\n>> ");
        String line = sc.next().trim();
        switch(line){
            case "1":
                System.out.println("Ptr. a anula o programare tastati 'A' in dreptul ei.\n -> Programari: ");
                Iterator<Program> iterator = client.getProgram().iterator();
                while(iterator.hasNext()){
                   Program p = iterator.next();
                   System.out.print(p + " ");
                   line = sc.next().trim();
                   if(line.equalsIgnoreCase("A")) {
                       Serializable[] command = new Serializable[]{"remove", p};
                       Serializable response = client.sendMessage(command);
                       if(!response.equals("fail"))
                           iterator.remove();
                   }
                }
                break;
            case "2":
                programare(client, sc);
                break;
        }
    }

    public static void programare(Client client, Scanner sc) throws Exception {
        Persoana persoana;
        if(client.getCont() == null){
            System.out.print("CNP: ");
            String cnp = sc.next().trim();

            if(!Utils.isCnp(cnp)){
                System.out.println("Cnp invalid!");
                return;
            }
            Serializable[] command = {"fetch", "Anexe.Tabele.Persoana", cnp};
            persoana = (Persoana) client.sendMessage(command);
            if(persoana == null){
                System.out.print("Nume: ");
                String nume = sc.next().trim();
                System.out.print("Prenume: ");
                String prenume = sc.next().trim();
                System.out.print("Contact: ");
                String contact = sc.next().trim();
                persoana = new Persoana(cnp, nume, prenume, "", contact);
                command = new Serializable[]{"add", persoana};
                Serializable response = client.sendMessage(command);
                UnexpectedException.checkResponse(response);
            }
        }
        else persoana = (Persoana) client.getSelf();

        String query = "SELECT DISTINCT m FROM Intervale i JOIN i.medic m";
        Serializable[] command = new Serializable[]{"select", query, "Anexe.Tabele.Medic", null};
        List<Medic> M = (List<Medic>) client.sendMessage(command);

        for(int i = 1; i <= M.size(); i++){
            System.out.println(i + ". " + M.get(i-1));
        }
        System.out.print(">> ");
        String line = sc.next().trim();
        if(line.isEmpty()) return;
        try{
            int l = Integer.parseInt(line);
            if(l >= 1 && l <= M.size()){
                l -= 1;
                Medic m = M.get(l);
                query = "SELECT p FROM Program p WHERE p.persoana = :persoana AND p.medic = :medic AND p.ziCalendaristica > :now AND p.ziCalendaristica < :then";
                LocalDate now = LocalDate.now();
                LocalDate then = LocalDate.now().plusDays(14);
                HashMap<String, Object> params = new HashMap<>();
                params.put("persoana", persoana);
                params.put("medic", m);
                params.put("now", now);
                params.put("then", then);
                command = new Serializable[]{"select", query, "Anexe.Tabele.Program", params};
                List<Program> program = (List<Program>) client.sendMessage(command);
                if(!program.isEmpty()){
                    System.out.println("Aveti deja o programare la " + m);
                    System.out.println(program.getFirst());
                    return;
                }

                params = new HashMap<>();
                params.put("medic", m);
                params.put("now", now);
                params.put("then", then);
                query = "SELECT p FROM Program p WHERE p.medic = :medic AND p.persoana IS NULL AND p.ziCalendaristica > :now AND p.ziCalendaristica < :then";
                command = new Serializable[]{"select", query, "Anexe.Tabele.Program", params};
                program = (List<Program>) client.sendMessage(command);
                HashMap<String, Program> Pmap = new HashMap<>();
                for(LocalDate date = now.plusDays(1); !date.isAfter(then); date = date.plusDays(1)){
                    if(date.getDayOfWeek().getValue() > 5) continue;
                    boolean ok = false;
                    for(Program p : program){
                        Pmap.put(p.getId(), p);
                        if(p.getZiCalendaristica().equals(date)){
                            if(!ok){
                                int d = date.getDayOfWeek().getValue();
                                System.out.print(ZiASaptamanii.get(d) + "(" + date + "): ");
                                ok = true;
                            }
                            System.out.print(" " + p.getStartTime() + "-" + p.getEndTime() + ";");
                        }
                    }
                    if(ok) System.out.println();
                }
                System.out.print("\nAlege data si ora programarii (aaaa-ll-zz oo:mm): ");
                
                String dataAleasa = sc.next().trim();
                String oraAleasa = sc.next().trim();
                line = dataAleasa + " " + oraAleasa;

                String[] L;
                LocalTime ora;
                try{
                    L = line.split(" ");
                    ora = LocalTime.parse(L[1]);
                }
                catch(Exception ex){
                    System.out.println("Format invalid!");
                    return;
                }
                String id = m.getId() + LocalDate.parse(L[0]) + ora + ora.plusMinutes(30);
                if(!Pmap.containsKey(id)){
                    System.out.println("Data selectata este invalida.");
                    return;
                }
                Program p = Pmap.get(id);
                p.setPersoana(persoana);
                command = new Serializable[]{"merge", p};
                Serializable response = client.sendMessage(command);
                UnexpectedException.checkResponse(response);
                System.out.println("Programare finalizata cu succes!");
            }
        } catch(NumberFormatException ex){
            System.out.println("Comanda invalida!");
        } catch(Exception ex){
            System.out.println("Eroare neasteptata! Operatie esuata.");
        }
    }
}