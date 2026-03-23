package com.corentin.garage_api.controller;

import com.corentin.garage_api.entity.Entretien;
import com.corentin.garage_api.entity.Moto;
import com.corentin.garage_api.service.MotoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class GarageController {

    private final MotoService motoService;

    public GarageController(MotoService motoService) {

        this.motoService = motoService;
    }

    @GetMapping("/bienvenue")
    public String direBonjour() {

        return "MotoCare API";
    }

    @PostMapping("/motos")
    public Moto ajouterUneMoto(@Valid @RequestBody Moto moto) {
        return motoService.ajouterMoto(moto);
    }

    @GetMapping("/motos")
    public List<Moto> listerMotos() {
        return motoService.recupererToutesLesMotos();
    }

    @DeleteMapping("/motos/{id}")
    public void supprimerMoto(@PathVariable Long id) {
        motoService.supprimerMoto(id);
    }

    @PutMapping("/motos/{id}")
    public Moto modifierUneMoto(@PathVariable Long id, @Valid @RequestBody Moto moto) {
        return this.motoService.mettreAJourMoto(id, moto);
    }

    @GetMapping("/motos/marque/{marque}")
    public List<Moto> listerParMarque(@PathVariable String marque) {
        return this.motoService.rechercherParMarque(marque);
    }

    @PostMapping("/motos/{id}/entretiens")
    public Entretien ajouterEntretienAUneMoto(@PathVariable Long id, @RequestBody Entretien entretien) {
        return this.motoService.ajouterEntretien(id, entretien);
    }

    @GetMapping("/motos/{id}/entretiens")
    public List<Entretien> recupererEntretien(@PathVariable Long id) {
        return this.motoService.recupererEntretiens(id);
    }

    @GetMapping("/motos/{id}/bilan")
        public List<String> consulterBilan(@PathVariable Long id) {
            return this.motoService.genererBilan(id);
    }
}