package com.corentin.garage_api.service;

import com.corentin.garage_api.entity.Entretien;
import com.corentin.garage_api.entity.Moto;
import com.corentin.garage_api.repository.EntretienRepository;
import com.corentin.garage_api.repository.MotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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