package Anexe.Tabele;

import Anexe.Entry;
import jakarta.persistence.*;
import java.time.*;

@SuppressWarnings({"unused", "JpaDataSourceORMInspection", "SpellCheckingInspection"})
@Entity @Table
public class Reprogramari extends Entry {
    @Id @Column private String id;
    @Column(name="zi_calendaristica") private LocalDate ziCalendaristica;
    @ManyToOne @JoinColumn private Medic medic;
    @Enumerated(EnumType.STRING) private DayOfWeek ziLucratoare;
    @Column(name="start_time") private LocalTime startTime;
    @Column(name="end_time") private LocalTime endTime;
    @ManyToOne @JoinColumn Persoana persoana;

    public Reprogramari() {}
    public Reprogramari(Program P) {
        this.ziCalendaristica = P.getZiCalendaristica();
        this.medic = P.getMedic();
        this.ziLucratoare = P.getZiLucratoare();
        this.startTime = P.getStartTime();
        this.endTime = P.getEndTime();
        this.id = P.getId();
    }

    @Override
    public String toString() {
        return  persoana.getNume() + persoana.getPrenume() + " - " + persoana.getContact() + "; " + medic;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Persoana getPersoana() {
        return persoana;
    }
    public void setPersoana(Persoana persoana) {
        this.persoana = persoana;
    }
}

