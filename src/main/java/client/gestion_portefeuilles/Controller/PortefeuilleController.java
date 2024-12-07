package client.gestion_portefeuilles.Controller;

import client.gestion_portefeuilles.Model.CarteVirtuelle;
import client.gestion_portefeuilles.Model.Devise;
import client.gestion_portefeuilles.Model.Portefeuille;
import client.gestion_portefeuilles.Repository.PortefeuilleRepository;
import client.gestion_portefeuilles.Service.PortefeuilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/gestion_portefeuille/portefeuille")
public class PortefeuilleController {

    PortefeuilleService portefeuilleService;
    private PortefeuilleRepository portefeuilleRepository;

    @Autowired
    public void PortefeuilleService(PortefeuilleService portefeuilleService) {
        this.portefeuilleService = portefeuilleService;
    }

    @GetMapping("/{id}")
    public Portefeuille getPortefeuille(@PathVariable int id){
        return portefeuilleService.getPortefeuille(id);
    }
    @PostMapping("/creer")
    public Portefeuille creerPorteFeuille(@RequestParam int utilisateurId, @RequestParam Devise devise, @RequestParam BigDecimal solde) {
        return portefeuilleService.creerPorteFeuille(utilisateurId, devise, solde);
    }

    @PostMapping("/ajouterCarte")
    public Portefeuille ajouterCarte(@RequestParam int id, @RequestBody CarteVirtuelle carte) {
        return portefeuilleService.ajouterCarte(id, carte);
    }

    @PostMapping("/deposer")
    public Portefeuille deposer(@RequestBody Portefeuille portefeuille, @RequestParam int somme) {
        return portefeuilleService.deposer(portefeuille, somme);
    }

    @PostMapping("/retirer")
    public Portefeuille retirer(@RequestBody Portefeuille portefeuille, @RequestParam int somme) {
        return portefeuilleService.retirer(portefeuille, somme);
    }
    @GetMapping("/{id}/cartes")
    public List<CarteVirtuelle> getCartes(@PathVariable int id) {
        return portefeuilleService.getCartes(portefeuilleRepository.findById(id));
    }
    @PostMapping("/convertir")
    public Portefeuille convertir(@RequestParam Devise target, @RequestBody Portefeuille portefeuille){
        return portefeuilleService.convertir(target,portefeuille);
    }

    @Autowired
    public void setPortefeuilleRepository(PortefeuilleRepository portefeuilleRepository) {
        this.portefeuilleRepository = portefeuilleRepository;
    }
}
