package Anexe.Tabele;

import Anexe.Entry;
import jakarta.persistence.*;
import java.time.*;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
@Entity @Table
public class Intervale extends Entry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nr;
    @ManyToOne @JoinColumn
    private Medic medic;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @Column private LocalTime startTime;
    @Column private LocalTime endTime;

    public Intervale() {}
    public Intervale(Medic medic, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.medic = medic;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString(){
        return "[" + medic.getId() + "] - " + dayOfWeek + ": " + startTime + " - " + endTime;
    }

    public Medic getMedic() {
        return medic;
    }
    public void setMedic(Medic medic) {
        this.medic = medic;
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
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
