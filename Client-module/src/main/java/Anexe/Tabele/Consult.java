package Anexe.Tabele;

import Anexe.Entry;
import jakarta.persistence.*;
import java.time.LocalDate;

@SuppressWarnings({"unused", "JpaDataSourceORMInspection", "SpellCheckingInspection"})
@Entity @Table(name="consulturi")
public class Consult extends Entry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column private Integer nr;
    @ManyToOne @JoinColumn private Medic medic;
    @ManyToOne @JoinColumn private Persoana persoana;
    @Column private String diagnostic;
    @Column private String detalii;
    @Column private LocalDate data;

    public Consult() {}
    public Consult(Medic medic, Persoana persoana, String diagnostic, String detalii, LocalDate data) {
        this.medic = medic;
        this.persoana = persoana;
        this.diagnostic = diagnostic;
        this.detalii = detalii;
        this.data = data;
    }

    @Override
    public String toString(){
        return data + ": ["+ medic.getId() + "] - " + persoana.getNume() + " " + persoana.getPrenume() + "\n"
                + "diagnostic: " + diagnostic + "\n" + "detalii: " + detalii + ";";
    }

    public Medic getMedic() {
        return medic;
    }
    public void setMedic(Medic medic) {
        this.medic = medic;
    }
    public Persoana getPersoana() {
        return persoana;
    }
    public void setPersoana(Persoana persoana) {
        this.persoana = persoana;
    }
    public String getDiagnostic() {
        return diagnostic;
    }
    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }
    public String getDetalii() {
        return detalii;
    }
    public void setDetalii(String detalii) {
        this.detalii = detalii;
    }
    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
}
