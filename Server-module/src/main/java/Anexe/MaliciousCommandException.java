package Anexe;

import java.io.Serializable;

public class MaliciousCommandException extends RuntimeException {
    public MaliciousCommandException(String message) {
        super(message);
    }

    public static void inspectCommand(Serializable[] command){
        StringBuilder cmd = new StringBuilder();
        for(Serializable s : command){
            cmd.append(s);
        }

        String commandString = cmd.toString();
        String sqlInjection = ".*([\\\\\"'`;%_$#&]|/\\*|\\*/|--).*";
        if(commandString.matches(sqlInjection)){
            throw new MaliciousCommandException("Malicious command detected: " + commandString);
        }
    }
}
