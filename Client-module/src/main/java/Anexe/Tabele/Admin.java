package Anexe.Tabele;

import Anexe.Entry;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@SuppressWarnings("unused")
@Entity @Table
public class Admin extends Entry {
    @Id @Column(nullable=false)
    private String id;
    @Column(nullable=false)
    private String pin;

    public Admin() {}
    public Admin(String id, String pin) {
        this.id = id;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPin(){
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
}