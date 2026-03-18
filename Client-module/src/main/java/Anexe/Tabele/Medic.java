package Anexe.Tabele;

import Anexe.Entry;
import jakarta.persistence.*;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
@Entity @Table
public class Medic extends Entry {
    @Id @Column(nullable=false) private String id;
    @Column private String specializare;

    public Medic() {}
    public Medic(String id, String specializare) {
        this.id = id;
        this.specializare = specializare;
    }

    @Override
    public String toString(){
        return id + " (" + specializare + ")";
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSpecializare() {
        return specializare;
    }
    public void setSpecializare(String specializare) {
        this.specializare = specializare;
    }
}
