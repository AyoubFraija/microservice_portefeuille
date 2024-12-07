package client.gestion_portefeuilles.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PortefeuilleTests {

    private Portefeuille portefeuille;
    private CarteVirtuelle carteVirtuelle;

    @BeforeEach
    void setUp() {
        portefeuille = new Portefeuille();
        portefeuille.setSolde(BigDecimal.valueOf(1000));
        portefeuille.setSolde(BigDecimal.valueOf(1000));
        portefeuille.setCartes(new ArrayList<>());

        carteVirtuelle = new CarteVirtuelle();
        carteVirtuelle.setNumeroCarte(123456789);
        carteVirtuelle.setDateExpiration(new Date(System.currentTimeMillis() + 100000));
        carteVirtuelle.setCvv(123);
        carteVirtuelle.setStatut(StatutCarteVirtuelle.INACTIVE);
    }

    @Test
    void testDeposer() {
        BigDecimal somme = BigDecimal.valueOf(500);
        portefeuille.deposer(somme);
        assertEquals(BigDecimal.valueOf(1500), portefeuille.getSolde());
    }

    @Test
    void testRetirer() {
        BigDecimal somme = BigDecimal.valueOf(500);
        portefeuille.retirer(somme);
        assertEquals(BigDecimal.valueOf(500), portefeuille.getSolde());
    }

    @Test
    void testRetirerInsufficientFunds() {
        BigDecimal somme = BigDecimal.valueOf(1500);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            portefeuille.retirer(somme);
        });
        assertEquals("Solde Insuffisant", exception.getMessage());
    }

    @Test
    void testAjouterCarte() {
        portefeuille.ajouterCarte(carteVirtuelle);
        assertTrue(portefeuille.getCartes().contains(carteVirtuelle));
    }

    @Test
    void testConvertir() {
        portefeuille.setSolde(BigDecimal.valueOf(1000));
        portefeuille.setDevise(Devise.EUR);

        portefeuille.convertir(Devise.USD, 1.1);

        assertEquals(Devise.USD, portefeuille.getDevise());
        assertEquals(BigDecimal.valueOf(1100).setScale(2), portefeuille.getSolde().setScale(2));
    }
}
