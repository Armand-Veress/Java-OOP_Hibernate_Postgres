package Client.Operatii;

import Anexe.Tabele.*;
import Anexe.UnexpectedException;
import Anexe.Utils;
import Client.Client;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@SuppressWarnings({"unchecked", "SpellCheckingInspection", "DuplicatedCode", "unused"})
public class OperatiiMedic {
    public static void nouConsult(Client client, Scanner sc) throws Exception {
        System.out.println(" -> Persoana consultata: ");
        System.out.print("CNP: ");
        String cnp = sc.next().trim();
        if(!Utils.isCnp(cnp)){
            System.out.println("Cnp invalid!");
            return;
        }

        String query = "SELECT p FROM Persoana p LEFT JOIN FETCH p.boliCronice WHERE p.cnp = :cnp";
        HashMap<String, Object> params = new HashMap<>();
        params.put("cnp", cnp);
        Serializable[] command = {"select", query, "Anexe.Tabele.Persoana", params};
        Serializable response = client.sendMessage(command);
        List<Persoana> persoana = (ArrayList<Persoana>) response;
        Persoana P;
        if(persoana == null || persoana.isEmpty()){
            System.out.print("Nume: ");
            String nume = sc.next().trim();
            System.out.print("Prenume: ");
            String prenume = sc.next().trim();
            System.out.print("Alergii: ");
            String alergii = sc.next().trim();
            System.out.print("Contact: ");
            String contact = sc.next().trim();
            P = new Persoana(cnp, nume, prenume, alergii, contact);
            P.setBoliCronice(new HashSet<>());
        }
        else {
            P = persoana.getFirst();
            System.out.println(P);
        }
        System.out.print("Diagnostic: ");
        String diagnostic = sc.next().trim();
        System.out.print("Detalii: ");
        String detalii = sc.next().trim();

        command = new Serializable[]{"reattachList", new ArrayList<>(client.getBoliCronice())};
        response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);
        client.setBoliCronice(new HashSet<>((List<BoliCronice>) response));

        for(BoliCronice B : client.getBoliCronice()){
            if(diagnostic.toLowerCase().contains(B.getDenumire().toLowerCase())) {
                P.getBoliCronice().removeIf(B2 -> B.getDenumire().equalsIgnoreCase(B2.getDenumire()));
                P.getBoliCronice().add(B);
            }
        }
        command = new Serializable[]{"reattach", P};
        response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);
        P = (Persoana) response;

        Consult c = new Consult((Medic) client.getSelf(), P, diagnostic, detalii, LocalDate.now());
        command = new Serializable[]{"add", c};
        response = client.sendMessage(command);
        UnexpectedException.checkResponse(response);
        client.getPacienti().remove(P);
        client.getPacienti().add(P);
        client.getConsultari().add(c);
    }

    public static void listaPacienti(Client client, Scanner sc) throws Exception {
        System.out.print(" Cautare: ");
        String line = sc.next().trim();
        line = line.toLowerCase();
        System.out.println(" -> Rezultate: ");
        if(line.isEmpty()){
            for(Persoana P : client.getPacienti())
                System.out.println("-> " + P.toString2());
            return;
        }
        List<Persoana> pacienti = new ArrayList<>();
        for(Persoana P : client.getPacienti()){
            if(P.getCnp().equals(line) | (P.getNume() + P.getPrenume()).toLowerCase().contains(line) | (P.getPrenume() + P.getNume()).toLowerCase().contains(line)){
                pacienti.add(P);
            }
        }
        if(pacienti.size() == 1){
            Persoana P = pacienti.getFirst();
            System.out.println("M - modifica / C - consulta.");
            System.out.print(pacienti.getFirst().toString2() + "; ");
            line = sc.next().trim();
            if(line.equalsIgnoreCase("M")){
                System.out.print("(Nume: " + P.getNume() + ")\n" + "Nume: ");
                line = sc.next().trim();
                if(!line.isEmpty()) {
                    P.setNume(line);
                }
                System.out.print("(Prenume: " + P.getPrenume() + ")\n" + "Prenume: ");
                line = sc.next().trim();
                if(!line.isEmpty()) {
                    P.setContact(line);
                }
                System.out.print("(Alergii: " + P.getAlergii() + ")\n" + "Alergii: ");
                line = sc.next().trim();
                if(!line.isEmpty()) {
                    P.setAlergii(line);
                }
                Serializable[] command = new Serializable[]{"merge", P};
                Serializable response = client.sendMessage(command);
                UnexpectedException.checkResponse(response);
            }
            else if(line.equalsIgnoreCase("C")){
                System.out.print("Diagnostic: ");
                String diagnostic = sc.next().trim();
                if(diagnostic.isEmpty()) return;
                System.out.print("Detalii: ");
                String detalii = sc.next().trim();

                Serializable[] command = new Serializable[]{"reattachList", new ArrayList<>(client.getBoliCronice())};
                Serializable response = client.sendMessage(command);
                UnexpectedException.checkResponse(response);

                client.setBoliCronice(new HashSet<>((List<BoliCronice>) response));
                for(BoliCronice B : client.getBoliCronice()){
                    if(diagnostic.toLowerCase().contains(B.getDenumire().toLowerCase())) {
                        P.getBoliCronice().removeIf(B2 -> B.getDenumire().equalsIgnoreCase(B2.getDenumire()));
                        P.getBoliCronice().add(B);
                    }
                }
                command = new Serializable[]{"reattach", P};
                response = client.sendMessage(command);
                UnexpectedException.checkResponse(response);

                P = (Persoana) response;
                Consult c = new Consult((Medic) client.getSelf(), P, diagnostic, detalii, LocalDate.now());
                command = new Serializable[]{"add", c};
                response = client.sendMessage(command);
                UnexpectedException.checkResponse(response);

                client.getPacienti().remove(P);
                client.getPacienti().add(P);
                client.getConsultari().add(c);
            }
        }
        else{
            for(Persoana P : pacienti)
                System.out.println(" -> " + P.toString2());
        }
    }

    public static void listaConsultari(Client client, Scanner sc){
        System.out.print(" Cautare: ");
        String line = sc.next().trim();

        System.out.println(" -> Rezultate: ");
        for(Consult C : client.getConsultari()){
            if(C.getPersoana().getCnp().equals(line) || (C.getPersoana().getNume() + C.getPersoana().getPrenume()).toLowerCase().contains(line) || (C.getPersoana().getPrenume() + C.getPersoana().getNume()).toLowerCase().contains(line)){
                System.out.println(" -> " + C);
            }
        }
    }

    public static void afisareProgram(Client client, Scanner sc) {
        if(LocalDate.now().getDayOfWeek().getValue() <= 5){
            System.out.println(" -> Ziua curenta:");
            for(Program p : client.getProgram()){
                if(p.getZiCalendaristica().equals(LocalDate.now()))
                    System.out.println(p + " - " + p.getPersoana());
            }
        }
        System.out.print("Verifica data / interval [aaaa-ll-zz / aaaa-ll-zz_aaaa-ll-zz]: ");
        String line = sc.next().trim();
        try{
            String[] L = line.split("_");
            if(L.length == 1){
                LocalDate d = LocalDate.parse(L[0]);
                System.out.println(" -> " + d + ":");
                for(Program p : client.getProgram()){
                    if(p.getZiCalendaristica().equals(d))
                        System.out.println(p + " - " + p.getPersoana().getNume() + " " + p.getPersoana().getPrenume() + "(" + p.getPersoana().getContact() + ")");
                }
            }
            else{
                LocalDate start = LocalDate.parse(L[0]);
                LocalDate end = LocalDate.parse(L[1]);
                System.out.println(" -> " + start + "_" + end + ":");
                for(Program p : client.getProgram()){
                    boolean bool = p.getZiCalendaristica().isAfter(start) && p.getZiCalendaristica().isBefore(end);
                    bool = bool || (p.getZiCalendaristica().isEqual(start) || p.getZiCalendaristica().isEqual(end));
                    bool = bool && p.getPersoana() != null;
                    if(bool) System.out.println(p + " - " + p.getPersoana());
                }
            }
        }
        catch(Exception e){
            System.out.println("Format invalid.");
        }
    }
}