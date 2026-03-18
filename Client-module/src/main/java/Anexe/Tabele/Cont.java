package Anexe.Tabele;

import Anexe.Entry;
import jakarta.persistence.*;

@SuppressWarnings({"unused", "JpaDataSourceORMInspection", "SpellCheckingInspection"})
@Entity @Table(name="conturi")
public class Cont extends Entry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nr;
    @Column(nullable=false) String rol;
    @Column(nullable=false) private String id;
    @Column(nullable=false) private String pin;
    @Column private Boolean isActiv;

    public Cont() {}
    public Cont(String rol, String id, String pin) {
        this.id = id;
        this.pin = pin;
        this.rol = rol;
        isActiv = false;
    }

    @Override
    public String toString(){
        return nr + ": " + "[" + rol + "] - " + id + ". Status: " + isActiv;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    public Boolean getIsActiv() {
        return isActiv;
    }
    public void setIsActiv(Boolean isActiv) {
        this.isActiv = isActiv;
    }
}
