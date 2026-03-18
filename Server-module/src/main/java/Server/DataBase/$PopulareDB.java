package Server.DataBase;

import Anexe.Tabele.*;
import Anexe.Utils;

import java.time.*;
import java.util.*;

@SuppressWarnings({"SpellCheckingInspection", "DuplicatedCode", "unused"})
public class $PopulareDB {
    public static void terminateQuery(){
        Database.nativeQuery("DROP TABLE IF EXISTS intervale", null);
        Database.nativeQuery("DROP TABLE IF EXISTS program", null);
        Database.nativeQuery("DROP TABLE IF EXISTS reprogramari", null);
        Database.nativeQuery("DROP TABLE IF EXISTS legatura_boli_pacienti", null);
        Database.nativeQuery("DROP TABLE IF EXISTS consulturi", null);
        Database.nativeQuery("DROP TABLE IF EXISTS admin", null);
        Database.nativeQuery("DROP TABLE IF EXISTS medic", null);
        Database.nativeQuery("DROP TABLE IF EXISTS persoana", null);
        Database.nativeQuery("DROP TABLE IF EXISTS boli_cronice", null);
        Database.nativeQuery("DROP TABLE IF EXISTS conturi", null);
    }
    public static void main(String[] args) {
        //terminateQuery();
        Database.nativeQuery("INSERT INTO boli_cronice (cod, denumire) VALUES" +
                "('E11', 'Diabet zaharat tip 2')," +
                "('I10', 'Hipertensiune arteriala')," +
                "('J45', 'Astm bronsic')," +
                "('M41', 'Scolioza')," +
                "('M51.2', 'Hernie de disc lombară')," +
                "('H52.1', 'Miopie');", null);

        Admin admin = new Admin("admin", Utils.hashPin("1234"));
        Database.addEntry(admin);
        Cont C = new Cont("admin", admin.getId(), admin.getPin());
        C.setIsActiv(true);
        Database.addEntry(C);

        Medic medic1 = new Medic("Dr. Medic1", "Medic dermatolog");
        Medic medic2 = new Medic("Dr. Medic2", "Medic ortoped");
        Medic medic3 = new Medic("Dr. Medic3", "Medic de familie");
        Medic medic4 = new Medic("Dr. Medic4", "Medic cardiolog");
        Medic RMN = new Medic("RMN", "cu bilet de trimitere");
        Medic CT = new Medic("CT", "cu bilet de trimitere");
        Medic radiografie = new Medic("Radiografie", "cu bilet de trimitere");
        Database.addEntry(medic1);
        Database.addEntry(medic2);
        Database.addEntry(medic3);
        Database.addEntry(medic4);
        Database.addEntry(RMN);
        Database.addEntry(CT);
        Database.addEntry(radiografie);

        C = new Cont("medic", medic1.getId(), Utils.hashPin("1111"));
        C.setIsActiv(true);
        Database.addEntry(C);
        C = new Cont("medic", medic2.getId(), Utils.hashPin("2222"));
        C.setIsActiv(true);
        Database.addEntry(C);
        C = new Cont("medic", medic3.getId(), Utils.hashPin("3333"));
        C.setIsActiv(true);
        Database.addEntry(C);
        C = new Cont("medic", medic4.getId(), Utils.hashPin("4444"));
        C.setIsActiv(true);
        Database.addEntry(C);
        C = new Cont("medic", RMN.getId(), Utils.hashPin("0000"));
        C.setIsActiv(true);
        Database.addEntry(C);
        C = new Cont("medic", CT.getId(), Utils.hashPin("0000"));
        C.setIsActiv(true);
        Database.addEntry(C);
        C = new Cont("medic", radiografie.getId(), Utils.hashPin("0000"));
        C.setIsActiv(true);
        Database.addEntry(C);

        Persoana persoana1 = new Persoana("5010101080011", "Ionescu", "Ion", "", "0772288229");
        Persoana persoana2 = new Persoana("5020202080022", "Vasilescu", "Vasile", "", "0763548322");
        Persoana persoana3 = new Persoana("4890303080033", "Anusca", "Ana", "", "0732456789");
        Persoana persoana4 = new Persoana("4670404080044", "Marinescu", "Marinela", "", "0721655743");
        Persoana persoana5 = new Persoana("6050505080055", "Florescu", "Florina", "polen", "0728165550");
        Database.addEntry(persoana1);
        Database.addEntry(persoana2);
        Database.addEntry(persoana3);
        Database.addEntry(persoana4);
        Database.addEntry(persoana5);
        C = new Cont("persoana", persoana1.getCnp(), Utils.hashPin("1000"));
        C.setIsActiv(true);
        Database.addEntry(C);
        C = new Cont("persoana", persoana2.getCnp(), Utils.hashPin("2000"));
        C.setIsActiv(true);
        Database.addEntry(C);
        C = new Cont("persoana", persoana3.getCnp(), Utils.hashPin("3000"));
        C.setIsActiv(true);
        Database.addEntry(C);

        Consult Ct = new Consult(medic1, persoana1, "Raceala", "Se recomanda un ceai", LocalDate.now());
        Database.addEntry(Ct);
        Ct = new Consult(medic3, persoana1, "Raceala", "Se recomanda un ceai", LocalDate.now());
        Database.addEntry(Ct);
        Ct = new Consult(medic3, persoana2, "Gripa A", "...", LocalDate.now());
        Database.addEntry(Ct);
        Ct = new Consult(medic3, persoana3, "Gripa B", "...", LocalDate.now());
        Database.addEntry(Ct);

        Intervale I1 = new Intervale(medic1, DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(12, 30));
        Database.addEntry(I1);
        I1 = new Intervale(medic1, DayOfWeek.THURSDAY, LocalTime.of(14, 30), LocalTime.of(19, 30));
        Database.addEntry(I1);
        Intervale I2 = new Intervale(medic2, DayOfWeek.MONDAY, LocalTime.of(15, 0), LocalTime.of(19, 0));
        Database.addEntry(I2);
        I2 = new Intervale(medic2, DayOfWeek.WEDNESDAY, LocalTime.of(15, 0), LocalTime.of(19, 0));
        Database.addEntry(I2);
        I2 = new Intervale(medic2, DayOfWeek.FRIDAY, LocalTime.of(15, 0), LocalTime.of(19, 0));
        Database.addEntry(I2);
        Intervale I3 = new Intervale(medic3, DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(13, 0));
        Database.addEntry(I3);
        Intervale I4 = new Intervale(medic4, DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(14, 0));
        Database.addEntry(I4);
        Intervale I5 = new Intervale(RMN, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(13, 0));
        Database.addEntry(I5);
        I5 = new Intervale(RMN, DayOfWeek.MONDAY, LocalTime.of(15, 0), LocalTime.of(20, 0));
        Database.addEntry(I5);
        Intervale I6 = new Intervale(CT, DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(13, 0));
        Database.addEntry(I6);
        I6 = new Intervale(CT, DayOfWeek.THURSDAY, LocalTime.of(15, 0), LocalTime.of(20, 0));
        Database.addEntry(I6);
        Intervale I7 = new Intervale(radiografie, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(13, 0));
        Database.addEntry(I7);
        I7 = new Intervale(radiografie, DayOfWeek.MONDAY, LocalTime.of(15, 0), LocalTime.of(20, 0));
        Database.addEntry(I7);

        genereazaProgram();
    }

    public static void genereazaProgram(){
        Set<LocalDate> zileLibere = Utils.getZileLibere(2025);
        LocalDate startDate = LocalDate.of(2025, 6, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        DayOfWeek D = startDate.getDayOfWeek();
        for (int d = 1; d <= 7; d++) {
            String query = "SELECT i FROM Intervale i WHERE i.dayOfWeek = :D";
            HashMap<String, Object> params = new HashMap<>();
            params.put("D", D);
            List<Intervale> I = Database.selectQuery(query, Intervale.class, params);

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(7)) {
                if (zileLibere.contains(date)) continue;
                for(Intervale i : I){
                    for(LocalTime ora = i.getStartTime(); !ora.isAfter(i.getEndTime()); ora = ora.plusMinutes(30)){
                        Program p = new Program(date, i.getMedic(), D, ora, ora.plusMinutes(30));
                        Database.addEntry(p);
                    }
                }
            }
            D = D.plus(1);
            startDate = startDate.plusDays(1);
        }
    }
}
