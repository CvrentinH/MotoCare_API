package com.corentin.garage_api.service;

import com.corentin.garage_api.entity.Entretien;
import com.corentin.garage_api.entity.Moto;
import com.corentin.garage_api.repository.EntretienRepository;
import com.corentin.garage_api.repository.MotoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class MotoService {

    private final EntretienRepository entretienRepository;
    private final MotoRepository motoRepository;

    public MotoService(MotoRepository motoRepository, EntretienRepository entretienRepository) {
        this.motoRepository = motoRepository;
        this.entretienRepository = entretienRepository;
    }

    public Moto ajouterMoto(Moto moto) {
        return this.motoRepository.save(moto);
    }

    public List<Moto> recupererToutesLesMotos() {
        return this.motoRepository.findAll();
    }

    public void supprimerMoto(Long id) {
        this.motoRepository.deleteById(id);
    }

    public Moto mettreAJourMoto(Long id, Moto nouvellesDonnees) {
        Moto motoExistante = this.motoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moto introuvable"));

        motoExistante.setMarque(nouvellesDonnees.getMarque());
        motoExistante.setModele(nouvellesDonnees.getModele());
        motoExistante.setKilometrage(nouvellesDonnees.getKilometrage());

        return this.motoRepository.save(motoExistante);
    }

    public List<String> genererBilan(Long motoId) {
        Moto moto = this.motoRepository.findById(motoId)
                .orElseThrow(() -> new RuntimeException("Moto introuvable"));

        List<String> alertes = new ArrayList<>();
        Map<String, Entretien> historiqueRecent = new HashMap<>();

        for (Entretien entretien : moto.getHistoriqueEntretiens()) {
            String type = entretien.getTypeIntervention();
            if (!historiqueRecent.containsKey(type) || entretien.getDate().isAfter(historiqueRecent.get(type).getDate())) {
                historiqueRecent.put(type, entretien);
            }
        }
        int km = moto.getKilometrage();
        //Chez soi
        verifierRegle(alertes, historiqueRecent.get("GRAISSAGE_CHAINE"), "Graissage chaîne", km, 1000, null);
        verifierRegle(alertes, historiqueRecent.get("TENSION_CHAINE"), "Tension chaîne", km, 1000, null);
        verifierRegle(alertes, historiqueRecent.get("SYSTEME_ELECTRIQUE"), "Système électrique", km, null, 30);
        verifierRegle(alertes, historiqueRecent.get("GRAISSAGE_CABLERIE"), "Graissage câblerie", km, 10000, 365);
        verifierRegle(alertes, historiqueRecent.get("FILTRE_AIR"), "Filtre à air", km, 12000, 730);

        //Garage
        verifierRegle(alertes, historiqueRecent.get("VIDANGE"), "Vidange & Filtre à huile", km, 6000, 365);
        verifierRegle(alertes, historiqueRecent.get("FREINS"), "Plaquettes & disques", km, 6000, null);
        verifierRegle(alertes, historiqueRecent.get("BOUGIES"), "Bougies", km, 12000, null);
        verifierRegle(alertes, historiqueRecent.get("SOUPAPES"), "Jeu aux soupapes", km, 24000, null);
        verifierRegle(alertes, historiqueRecent.get("PNEUS"), "Pneus", km, 10000, null);
        verifierRegle(alertes, historiqueRecent.get("FOURCHE"), "Huile de fourche & joints", km, 20000, 730);
        verifierRegle(alertes, historiqueRecent.get("ROULEMENTS"), "Roulements", km, 20000, null);
        verifierRegle(alertes, historiqueRecent.get("KIT_CHAINE"), "Kit chaîne/courroie", km, 20000, null);
        verifierRegle(alertes, historiqueRecent.get("LIQUIDE_FREIN"), "Liquide de frein", km, null, 730);
        verifierRegle(alertes, historiqueRecent.get("LIQUIDE_REFROIDISSEMENT"), "Liquide de refroidissement", km, null, 730);

        if (alertes.isEmpty()) {
            alertes.add("Prête à rouler");
        }
        return alertes;
    }

    private void verifierRegle(List<String> alertes, Entretien dernierEntretien, String nomPiece, int kmActuel, Integer limiteKm, Integer limiteJours) {
        if (dernierEntretien == null) {
            alertes.add(nomPiece + " : Aucune trace dans le carnet d'entretien !");
            return;
        }

        boolean alerteDeclenchee = false;
        if (limiteKm != null) {
            int kmParcourus = kmActuel - dernierEntretien.getKilometrageMoto();
            if (kmParcourus >= limiteKm) {
                alertes.add(nomPiece + " : À faire (Dépassement de " + (kmParcourus - limiteKm) + " km)");
                alerteDeclenchee = true;
            }
        }

        if (limiteJours != null && !alerteDeclenchee) {
            long joursEcoules = ChronoUnit.DAYS.between(dernierEntretien.getDate(), LocalDate.now());
            if (joursEcoules >= limiteJours) {
                alertes.add(nomPiece + " : À faire (fait il y a " + joursEcoules + " jours)");
            }
        }
    }

    public List<Moto> rechercherParMarque(String marque) {
        return this.motoRepository.findByMarque(marque);
    }

    public Entretien ajouterEntretien(Long motoId, Entretien nouvelEntretien) {
        Moto moto = this.motoRepository.findById(motoId)
                .orElseThrow(() -> new RuntimeException("Moto introuvable"));

        nouvelEntretien.setMoto(moto);

        return this.entretienRepository.save(nouvelEntretien);
    }

    public List<Entretien> recupererEntretiens(Long motoId) {
        Moto moto = this.motoRepository.findById(motoId)
                .orElseThrow(() -> new RuntimeException("Entretien introuvable"));
        return moto.getHistoriqueEntretiens();
    }
}