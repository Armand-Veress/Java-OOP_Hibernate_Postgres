package Anexe.Tabele;

import Anexe.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unused", "JpaDataSourceORMInspection", "SpellCheckingInspection"})
@Entity @Table
public class Persoana extends Entry {
    @Id @Column(nullable=false) private String cnp;
    @Column private String nume;
    @Column private String prenume;
    @Column private Character sex;
    @Column private String alergii;
    @Column private String contact;
    @Column private LocalDate dataNastere;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) @JoinTable(
            name = "legatura_boli_pacienti",
            joinColumns = @JoinColumn(name="cnp_pacient"),
            inverseJoinColumns = @JoinColumn(name="cod_boala")
    )
    private Set<BoliCronice> boliCronice = new HashSet<>();

    public Persoana() {}
    public Persoana(String cnp, String nume, String prenume, String alergii, String contact) {
        this.cnp = cnp;
        this.nume = nume;
        this.prenume = prenume;
        this.sex = Utils.sex(cnp);
        this.alergii = alergii;
        this.contact = contact;
        this.dataNastere = Utils.dataNastere(cnp);
    }

    @Override
    public String toString(){
        return nume + " " + prenume + " (" + cnp + ")" + "\n"
                + sex + ", " + dataNastere + ", " + contact + "\n"
                + "Alergii: " + alergii;
    }

    public String getCnp() {
        return cnp;
    }
    public void setCnp(String cnp) {
        this.cnp = cnp;
    }
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public String getPrenume() {
        return prenume;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }
    public char getSex() {
        return sex;
    }
    public void setSex(char sex) {
        this.sex = sex;
    }
    public String getAlergii() {
        return alergii;
    }
    public void setAlergii(String alergii) {
        this.alergii = alergii;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public LocalDate getDataNastere() {
        return dataNastere;
    }
    public void setDataNastere(LocalDate dataNastere) {
        this.dataNastere = dataNastere;
    }
    public Set<BoliCronice> getBoliCronice() {
        return boliCronice;
    }
    public void setBoliCronice(Set<BoliCronice> boliCronice) {
        this.boliCronice = boliCronice;
    }

    public String toString2(){
        return nume + " " + prenume + " (" + cnp + ")" + "\n"
                + sex + ", " + dataNastere + ", " + contact + "\n"
                + "Alergii: " + alergii + "; Boli cronice: " + boliCronice;
    }
}
