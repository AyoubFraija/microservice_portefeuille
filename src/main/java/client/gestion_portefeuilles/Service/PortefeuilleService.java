package client.gestion_portefeuilles.Service;

import client.gestion_portefeuilles.Model.CarteVirtuelle;
import client.gestion_portefeuilles.Model.Devise;
import client.gestion_portefeuilles.Model.Portefeuille;
import client.gestion_portefeuilles.Repository.CarteVirtuelleRepository;
import client.gestion_portefeuilles.Repository.PortefeuilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PortefeuilleService {

    @Autowired
    private PortefeuilleRepository portefeuilleRepository;
    @Autowired
    private CarteVirtuelleRepository carteVirtuelleRepository;

    @Transactional
    public Portefeuille creerPorteFeuille(int utilisateurId, Devise devise, BigDecimal solde){
        Portefeuille portefeuille = new Portefeuille();
        portefeuille.setDevise(devise);
        portefeuille.setSolde(solde);
        portefeuille.setUtilisateurId(utilisateurId);
        portefeuilleRepository.save(portefeuille);
        return portefeuille;
    }
    @Transactional
    public Portefeuille ajouterCarte(int id,CarteVirtuelle carte){
        if (carte == null) {
            throw new NullPointerException("Carte cannot be null");
        }
        Portefeuille portefeuille = portefeuilleRepository.findById(id);
        portefeuille.ajouterCarte(carte);
        portefeuilleRepository.save(portefeuille);
        return portefeuille;
    }
    @Transactional
    public Portefeuille deposer(Portefeuille portefeuille, int somme){
        if (portefeuille == null) {
            throw new NullPointerException("Portefeuille cannot be null");
        }
        portefeuille.deposer(BigDecimal.valueOf(somme));
        portefeuilleRepository.save(portefeuille);
        return portefeuille;
    }
    @Transactional
    public Portefeuille retirer(Portefeuille portefeuille, int somme){
        if (portefeuille == null) {
            throw new NullPointerException("Portefeuille cannot be null");
        }
        portefeuille.retirer(BigDecimal.valueOf(somme));
        portefeuilleRepository.save(portefeuille);
        return portefeuille;
    }
    @Transactional
    public Portefeuille getPortefeuille(int id){
        return portefeuilleRepository.getByUtilisateurId(id);
    }
    @Transactional
    public List<CarteVirtuelle> getCartes(Portefeuille portefeuille){
        return portefeuilleRepository.findCarteVirtuellesById(portefeuille.getId());
    }
    @Transactional
    public Portefeuille convertir(Devise target,Portefeuille portefeuille){
        String base = portefeuille.getDevise().toString();
        String devise = target.toString();
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://exchange-rates.abstractapi.com/v1/live/?api_key=f9786150a3f945a1878d60db5ff99dde&base=" + base + "&target=" + devise;
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        String jsonString = response.getBody();
        JSONObject jsonObject = new JSONObject(jsonString);
        double rate = jsonObject.getJSONObject("exchange_rates").getDouble(devise);
        portefeuille.convertir(target,rate);
        portefeuilleRepository.save(portefeuille);
        return portefeuille;
    }
}
