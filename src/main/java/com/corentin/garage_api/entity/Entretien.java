package com.corentin.garage_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Entretien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private int kilometrageMoto;

    private String typeIntervention;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "moto_id")
    @JsonIgnore
    private Moto moto;

    public Entretien() {
    }

    public Entretien(Long id, LocalDate date, int kilometrageMoto, String typeIntervention, String notes) {
        this.id = id;
        this.date = date;
        this.kilometrageMoto = kilometrageMoto;
        this.typeIntervention = typeIntervention;
        this.notes = notes;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getKilometrageMoto() {
        return this.kilometrageMoto;
    }
    public void setKilometrageMoto(int kilometrageMoto) {
        this.kilometrageMoto = kilometrageMoto;
    }

    public String getTypeIntervention() {
        return this.typeIntervention;
    }
    public void setTypeIntervention(String typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    public String getNotes() {
        return this.notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Moto getMoto() {
        return moto;
    }

    public void setMoto(Moto moto) {
        this.moto = moto;
    }
}