package client.gestion_portefeuilles.Service;

import client.gestion_portefeuilles.Model.CarteVirtuelle;
import client.gestion_portefeuilles.Model.Portefeuille;
import client.gestion_portefeuilles.Repository.CarteVirtuelleRepository;
import client.gestion_portefeuilles.Repository.PortefeuilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Calendar;

@Service
public class CarteVirtuelleService {
    @Autowired
    private PortefeuilleRepository portefeuilleRepository;
    @Autowired
    private CarteVirtuelleRepository carteVirtuelleRepository;
    @Autowired
    private PortefeuilleService portefeuilleService;

    public CarteVirtuelle genererCarte(int somme){
        CarteVirtuelle carte = new CarteVirtuelle();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        carte.setDateExpiration(calendar.getTime());
        carte.setSolde(BigDecimal.valueOf(somme));
        SecureRandom secureRandom = new SecureRandom();
        int cvv = secureRandom.nextInt(900) + 100;
        carte.setCvv(cvv);
        carte.activer();
        carteVirtuelleRepository.save(carte);
        return carte;
    }

    @Transactional
    public CarteVirtuelle activer(CarteVirtuelle carte){
        carte.activer();
        carteVirtuelleRepository.save(carte);
        return carte;
    }
    @Transactional
    public CarteVirtuelle desactiver(CarteVirtuelle carte){
        carte.desactiver();
        carteVirtuelleRepository.save(carte);
        return carte;
    }
    @Transactional
    public CarteVirtuelle verify_expiration(CarteVirtuelle carte){
        carte.verify_expiration();
        carteVirtuelleRepository.save(carte);
        return carte;
    }
    @Transactional
    public CarteVirtuelle retirer(CarteVirtuelle carte, int somme){
        carte.retirer(BigDecimal.valueOf(somme));
        carteVirtuelleRepository.save(carte);
        portefeuilleRepository.save(carte.getPortefeuille());
        return carte;
    }

}
