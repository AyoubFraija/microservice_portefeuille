package client.gestion_portefeuilles.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class CarteVirtuelleTests {

    private CarteVirtuelle carteVirtuelle;

    @BeforeEach
    void setUp() {
        Portefeuille portefeuille = new Portefeuille();
        portefeuille.setSolde(BigDecimal.valueOf(1000));
        carteVirtuelle = new CarteVirtuelle();
        carteVirtuelle.setDateExpiration(new Date(System.currentTimeMillis() + 100000));
        carteVirtuelle.setStatut(StatutCarteVirtuelle.INACTIVE);
        carteVirtuelle.setSolde(BigDecimal.valueOf(1000));
        carteVirtuelle.setPortefeuille(portefeuille);
    }

    @Test
    void testActiver() {
        carteVirtuelle.activer();
        assertEquals(StatutCarteVirtuelle.ACTIVE, carteVirtuelle.getStatut());
    }

    @Test
    void testDesactiver() {
        carteVirtuelle.activer(); // First activate the card
        carteVirtuelle.desactiver();
        assertEquals(StatutCarteVirtuelle.INACTIVE, carteVirtuelle.getStatut());
    }

    @Test
    void testVerifyExpirationNotExpired() {
        carteVirtuelle.verify_expiration();
        assertNotEquals(StatutCarteVirtuelle.EXPIREE, carteVirtuelle.getStatut());
    }

    @Test
    void testVerifyExpirationExpired() {
        carteVirtuelle.setDateExpiration(new Date(System.currentTimeMillis() - 100000)); // Set expiration date in the past
        carteVirtuelle.verify_expiration();
        assertEquals(StatutCarteVirtuelle.EXPIREE, carteVirtuelle.getStatut());
    }

    @Test
    void testDeposer() {
        carteVirtuelle.deposer(BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(1500), carteVirtuelle.getSolde());
    }

    @Test
    void testRetirer() {
        carteVirtuelle.retirer(BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500), carteVirtuelle.getSolde());
        assertEquals(BigDecimal.valueOf(500), carteVirtuelle.getPortefeuille().getSolde());
    }
}