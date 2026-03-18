package Anexe.Tabele;

import Anexe.Entry;
import Anexe.ZiASaptamanii;
import jakarta.persistence.*;
import java.time.*;

@SuppressWarnings({"unused", "JpaDataSourceORMInspection", "SpellCheckingInspection"})
@Entity @Table
public class Program extends Entry {
    @Id @Column private String id;
    @Column(name="zi_calendaristica") private LocalDate ziCalendaristica;
    @ManyToOne @JoinColumn(name="id_medic") private Medic medic;
    @Enumerated(EnumType.STRING) private DayOfWeek ziLucratoare;
    @Column(name="start_time") private LocalTime startTime;
    @Column(name="end_time") private LocalTime endTime;
    @ManyToOne @JoinColumn Persoana persoana;

    public Program() {}
    public Program(LocalDate ziCalendaristica, Medic medic, DayOfWeek ziLucratoare, LocalTime startTime, LocalTime endTime) {
        this.ziCalendaristica = ziCalendaristica;
        this.medic = medic;
        this.ziLucratoare = ziLucratoare;
        this.startTime = startTime;
        this.endTime = endTime;
        this.id = this.medic.getId() + this.ziCalendaristica + this.startTime + this.endTime;
    }

    @Override
    public String toString() {
        return ziCalendaristica + " (" + ZiASaptamanii.get(ziLucratoare.getValue()) + "): " + startTime + "-" + endTime;
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
    public LocalDate getZiCalendaristica() {
        return ziCalendaristica;
    }
    public void setZiCalendaristica(LocalDate ziCalendaristica) {
        this.ziCalendaristica = ziCalendaristica;
    }
    public Medic getMedic() {
        return medic;
    }
    public void setMedic(Medic medic) {
        this.medic = medic;
    }
    public DayOfWeek getZiLucratoare() {
        return ziLucratoare;
    }
    public void setZiLucratoare(DayOfWeek ziLucratoare) {
        this.ziLucratoare = ziLucratoare;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
