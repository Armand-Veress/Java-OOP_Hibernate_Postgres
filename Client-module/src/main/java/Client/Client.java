package Client;

import Anexe.Entry;
import Anexe.Tabele.*;
import java.io.*;
import java.net.Socket;
import java.util.Set;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class Client {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Cont cont;
    private Entry self;

    private Set<BoliCronice> boliCronice;
    private Set<Persoana> pacienti;
    private Set<Consult> consultari;
    private Set<Cont> conturi;
    private Set<Medic> medici;
    private Set<Program> program;

    public Client() {}
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public Serializable sendMessage(Serializable message) throws IOException, ClassNotFoundException {
        out.writeObject(message);
        return (Serializable) in.readObject();
    }

    public void sendError(Serializable error) throws IOException {
        out.writeObject(error);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public void logOut(){
        boliCronice = null;
        pacienti = null;
        consultari = null;
        self = null;
        cont = null;
        conturi = null;
        medici = null;
        program = null;
    }

    public Cont getCont() {
        return cont;
    }
    public void setCont(Cont cont) {
        this.cont = cont;
    }
    public Entry getSelf() {
        return self;
    }
    public void setSelf(Entry self) {
        this.self = self;
    }
    public Set<BoliCronice> getBoliCronice() {
        return boliCronice;
    }
    public void setBoliCronice(Set<BoliCronice> boliCronice) {
        this.boliCronice = boliCronice;
    }
    public Set<Persoana> getPacienti() {
        return pacienti;
    }
    public void setPacienti(Set<Persoana> pacienti) {
        this.pacienti = pacienti;
    }
    public Set<Consult> getConsultari() {
        return consultari;
    }
    public void setConsultari(Set<Consult> consultari) {
        this.consultari = consultari;
    }
    public Set<Cont> getConturi() {
        return conturi;
    }
    public void setConturi(Set<Cont> conturi) {
        this.conturi = conturi;
    }
    public Set<Medic> getMedici() {
        return medici;
    }
    public void setMedici(Set<Medic> medici) {
        this.medici = medici;
    }
    public Set<Program> getProgram() {
        return program;
    }
    public void setProgram(Set<Program> program) {
        this.program = program;
    }
}
