package com.corentin.garage_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class Moto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La marque ne peut pas être vide")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @Min(value = 0, message = "Une moto ne peut pas avoir un kilométrage négatif")
    private int kilometrage;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entretien> historiqueEntretiens;

    public Moto(List<Entretien> historiqueEntretiens) {

        this.historiqueEntretiens = historiqueEntretiens;
    }

    public Moto(String marque, String modele, int kilometrage, List<Entretien> historiqueEntretiens) {
        this.marque = marque;
        this.modele = modele;
        this.kilometrage = kilometrage;
        this.historiqueEntretiens = historiqueEntretiens;
    }

    public Moto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(int kilometrage) {
        this.kilometrage = kilometrage;
    }

    public List<Entretien> getHistoriqueEntretiens() {
        return historiqueEntretiens;
    }

    public void setHistoriqueEntretiens(List<Entretien> historiqueEntretiens) {
        this.historiqueEntretiens = historiqueEntretiens;
    }
}
