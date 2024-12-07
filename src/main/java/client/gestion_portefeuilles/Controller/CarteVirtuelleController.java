package client.gestion_portefeuilles.Controller;

import client.gestion_portefeuilles.Model.CarteVirtuelle;
import client.gestion_portefeuilles.Service.CarteVirtuelleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestion_portefeuille/carte_virtuelle")
public class CarteVirtuelleController {

    @Autowired
    private CarteVirtuelleService carteVirtuelleService;

    @PostMapping("/generer")
    public CarteVirtuelle genererCarte(@RequestParam int somme) {
        return carteVirtuelleService.genererCarte(somme);
    }

    @PostMapping("/activer")
    public CarteVirtuelle activer(@RequestBody CarteVirtuelle carte) {
        return carteVirtuelleService.activer(carte);
    }

    @PostMapping("/desactiver")
    public CarteVirtuelle desactiver(@RequestBody CarteVirtuelle carte) {
        return carteVirtuelleService.desactiver(carte);
    }

    @PostMapping("/verify_expiration")
    public CarteVirtuelle verifyExpiration(@RequestBody CarteVirtuelle carte) {
        return carteVirtuelleService.verify_expiration(carte);
    }

    @PostMapping("/retirer")
    public CarteVirtuelle retirer(@RequestBody CarteVirtuelle carte, @RequestParam int somme) {
        return carteVirtuelleService.retirer(carte, somme);
    }
}