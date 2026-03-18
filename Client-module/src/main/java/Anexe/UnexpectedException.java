package Anexe;

import java.io.Serializable;

@SuppressWarnings("SpellCheckingInspection")
public class UnexpectedException extends RuntimeException {
    public UnexpectedException(String message) {
        super(message);
    }

    public static void checkResponse(Serializable response){
        if(response.equals("fail"))
            throw new UnexpectedException("Eroare neasteptata!. Operatie esuata.");
    }
}
