package Anexe;

import java.util.*;
import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;

@SuppressWarnings("SpellCheckingInspection")
public class Utils {
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

    //** jBCrypt hashing **
    public static String hashPin(String pin){
        return BCrypt.hashpw(pin, BCrypt.gensalt());
    }

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
}

