package Anexe.Tabele;

import Anexe.Entry;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@SuppressWarnings({"unused", "JpaDataSourceORMInspection", "SpellCheckingInspection"})
@Entity @Table(name="boli_cronice")
public final class BoliCronice extends Entry {
    @Id @Column
    private String cod;
    @Column
    private String denumire;

    public BoliCronice() {}
    public BoliCronice(String cod, String denumire) {
        this.cod = cod;
        this.denumire = denumire;
    }

    @Override
    public String toString(){
        return denumire + "(" + cod + ")";
    }

    public String getCod() {
        return cod;
    }
    public void setCod(String cod) {
        this.cod = cod;
    }
    public String getDenumire() {
        return denumire;
    }
    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
}
