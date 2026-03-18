package Client.Operatii;

import Anexe.Tabele.*;
import Anexe.UnexpectedException;
import Anexe.Utils;
import Anexe.ZiASaptamanii;
import Client.Client;
import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

@SuppressWarnings({"unchecked", "SpellCheckingInspection", "DuplicatedCode"})
public class OperatiiAdmin {
    public static void newAdmin(Client client, Scanner sc) throws Exception {
        System.out.print("Admin-Id: ");
        String id = sc.next().trim();
        if(id.isEmpty()) return;
        System.out.print("Introduceti un pin format din 4 cifre: ");
        String pin = sc.next().trim();
        while(!Utils.isPin(pin)){
            System.out.println("Pin invalid! (ex. 1234) - 4 cifre (0->9) ");
            System.out.print("Introduceti un pin format din 4 cifre: ");
            pin = sc.next().trim();
        }
        Admin A = new Admin(id, Utils.hashPin(pin));
        Cont C = new Cont("admin", id, Utils.hashPin(pin));
        C.setIsActiv(true);
        Serializable[] command = {"add", A};
        Serializable response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);

        command = new Serializable[]{"add", C};
        response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);
    }

    public static void newMedic(Client client, Scanner sc) throws Exception {
        System.out.print("Medic-Id: ");
        String id = sc.next().trim();
        if(id.isEmpty()) return;
        System.out.print("Specializare: ");
        String specializare = sc.next().trim();
        System.out.print("Introduceti un pin temporar format din 4 cifre: ");
        String pin = sc.next().trim();
        while(!Utils.isPin(pin)){
            System.out.println("Pin invalid! (ex. 1234) - 4 cifre (0->9) ");
            System.out.print("Introduceti un pin format din 4 cifre: ");
            pin = sc.next().trim();
        }
        Medic M = new Medic(id, specializare);
        Cont C = new Cont("medic", id, Utils.hashPin(pin));
        C.setIsActiv(true);
        Serializable[] command = {"add", M};
        Serializable response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);

        command = new Serializable[]{"add", C};
        response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);
    }

    public static void tratareSolicitari(Client client, Scanner sc) throws Exception {
        Set<Cont> C = client.getConturi();
        System.out.println(" -> Solicitari cont nou: ");
        for(Cont c : C){
            if(!c.getIsActiv())
                System.out.println(c);
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.println("1. Valideaza toate solicitarile ");
        System.out.print("2. Trateaza individual \n>> ");
        String line = sc.next().trim();
        Serializable[] command;
        Serializable response;
        switch(line){
            case "1":
                for(Cont c : C){
                    c.setIsActiv(true);
                }
                String query = "UPDATE Cont SET isActiv = true";
                command = new Serializable[]{"update", query, null};
                response = client.sendMessage(command);
                UnexpectedException.checkResponse(response);
                break;
            case "2":
                Iterator<Cont> iterator = C.iterator();
                while(iterator.hasNext()){
                    Cont c = iterator.next();
                    while(true){
                        System.out.println(c);
                        System.out.print("V - valideaza / R - respinge: ");
                        line = sc.next().trim();
                        if(line.equalsIgnoreCase("V")){
                            c.setIsActiv(true);
                            command = new Serializable[]{"merge", c};
                            response = client.sendMessage(command);
                            UnexpectedException.checkResponse(response);
                            break;
                        }
                        else if(line.equalsIgnoreCase("R")) {
                            iterator.remove();
                            command = new Serializable[]{"remove", c};
                            response = client.sendMessage(command);
                            UnexpectedException.checkResponse(response);
                            break;
                        }
                        else if(!line.isEmpty())
                            System.out.println("Comanda invalida! (ex. V / R)");
                    }
                }
                break;
        }
    }

    public static void genereazaProgram(Client client, Scanner sc) throws Exception {
        System.out.print("Genereaza din data de [aaaa-ll-zz]: ");
        String start = sc.next().trim();
        System.out.print("Pana in data de [aaaa-ll-zz]: ");
        String end = sc.next().trim();
        LocalDate startDate;
        LocalDate endDate;
        try{
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
            if(startDate.isBefore(LocalDate.now())) startDate = LocalDate.now().plusDays(1);

            HashMap<String, Object> params = new HashMap<>();
            params.put("startDate", startDate);
            String query = "SELECT p FROM Program p WHERE p.ziCalendaristica >= :startDate ";
            query += "AND p.persoana IS NOT NULL";
            Serializable[] command = {"select", query, "Anexe.Tabele.Program", params};
            Serializable response = client.sendMessage(command);
            UnexpectedException.checkResponse(response);
            Set<Program> P = new HashSet<>((List<Program>) response);
            HashMap<String, Program> Pmap = new HashMap<>();
            for(Program p : P){
                Pmap.put(p.getId(), p);
            }
            query = "DELETE FROM Program p WHERE p.ziCalendaristica >= :startDate";
            command = new Serializable[]{"update", query, params};
            response = client.sendMessage(command);
            UnexpectedException.checkResponse(response);
            genereazaProgram(client, startDate, endDate, Pmap);
        }
        catch(DateTimeParseException ex){
            System.out.println("Format invalid!");
        }
    }
    public static void genereazaProgram(Client client, LocalDate startDate, LocalDate endDate, HashMap<String, Program> Pmap) throws Exception {
        Set<LocalDate> zileLibere = Utils.getZileLibere(2025);
        DayOfWeek D = startDate.getDayOfWeek();
        for (int d = 1; d <= 7; d++) {
            String query = "SELECT i FROM Intervale i WHERE i.dayOfWeek = :D";
            HashMap<String, Object> params = new HashMap<>();
            params.put("D", D);
            Serializable[] command = {"select", query, "Anexe.Tabele.Intervale", params};
            Serializable response = client.sendMessage(command);
            UnexpectedException.checkResponse(response);
            List<Intervale> I = (List<Intervale>) response;

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(7)) {
                if (zileLibere.contains(date)) continue;
                for(Intervale i : I){
                    for(LocalTime ora = i.getStartTime(); !ora.isAfter(i.getEndTime()); ora = ora.plusMinutes(30)){
                        Program p = new Program(date, i.getMedic(), D, ora, ora.plusMinutes(30));
                        if(Pmap.containsKey(p.getId())){
                            p.setPersoana(Pmap.get(p.getId()).getPersoana());
                            Pmap.remove(p.getId());
                        }
                        command = new Serializable[]{"add", p};
                        response = client.sendMessage(command);
                        UnexpectedException.checkResponse(response);
                    }
                }
            }
            D = D.plus(1);
            startDate = startDate.plusDays(1);
        }
        for(String id : Pmap.keySet()){
            Reprogramari R = new Reprogramari(Pmap.get(id));
            Serializable[] command = {"add", R};
            Serializable response = client.sendMessage(command);
            UnexpectedException.checkResponse(response);
        }
    }

    public static void reprogramari(Client client, Scanner sc) throws Exception {
        String query = "SELECT r from Reprogramari r";
        Serializable[] command = {"select", query, null};
        Serializable response = client.sendMessage(command);
        List<Reprogramari> R = (List<Reprogramari>) response;
        Iterator<Reprogramari> iterator = R.iterator();
        while(iterator.hasNext()) {
            Reprogramari r = iterator.next();
            System.out.println(r.toString());
            System.out.print("Sterge - (da / nu): ");
            String line = sc.next().trim();
            if(line.equalsIgnoreCase("da"))
                iterator.remove();
        }
    }
    public static void program(Client client, Scanner sc) throws Exception {
        System.out.println("1. Adauga/Schimba interval de lucru");
        System.out.println("2. Genereaza program");
        System.out.print("3. Reprogramari\n>> ");
        String line = sc.next().trim();
        switch(line){
            case "1":
                interval(client, sc);
                break;
            case "2":
                genereazaProgram(client, sc);
                break;
            case "3":
                reprogramari(client, sc);
                break;
        }
    }

    public static void interval(Client client, Scanner sc) throws Exception {
        System.out.print("Id-medic: ");
        String id = sc.next().trim();
        Serializable[] command = {"fetch", "Anexe.Tabele.Medic", id};
        Serializable response = client.sendMessage(command);
        if(response == null){
            System.out.print("Id invalid.");
            return;
        }
        UnexpectedException.checkResponse(response);
        Medic M = (Medic) response;
        String query = "SELECT i FROM Intervale i WHERE i.medic = :medic";
        HashMap<String, Object> params = new HashMap<>();
        params.put("medic", M);
        command = new Serializable[]{"select", query, "Anexe.Tabele.Intervale", params};
        response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);
        List<Intervale> intervale = (List<Intervale>) response;

        System.out.print("Zi a saptamanii: ");
        String zi = sc.next().trim();
        System.out.println("Interval (oo:mm-oo:mm): ");
        String interval = sc.next().trim();
        String[] L = interval.split("-");
        try{
            Intervale I = new Intervale(M, DayOfWeek.of(ZiASaptamanii.getValue(zi)), LocalTime.parse(L[0]), LocalTime.parse(L[1]));
            for(Intervale i : intervale){
                if(i.getDayOfWeek() == DayOfWeek.of(ZiASaptamanii.getValue(zi))){
                    command = new Serializable[]{"remove", i};
                    client.sendMessage(command);
                }
            }
            command = new Serializable[]{"add", I};
            response = client.sendMessage(command);
            UnexpectedException.checkResponse(response);
        } catch(DateTimeException ex){
            System.out.println("Format invalid!");
        } catch(Exception ex){
            for(Intervale i : intervale){
                command = new Serializable[]{"merge", i};
                client.sendMessage(command);
            }
            throw ex;
        }
    }
}