package client.gestion_portefeuilles.Service;
import client.gestion_portefeuilles.Model.CarteVirtuelle;
import client.gestion_portefeuilles.Model.Devise;
import client.gestion_portefeuilles.Model.Portefeuille;
import client.gestion_portefeuilles.Model.StatutCarteVirtuelle;
import client.gestion_portefeuilles.Repository.CarteVirtuelleRepository;
import client.gestion_portefeuilles.Repository.PortefeuilleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CarteVirtuelleServiceTests {

    @Mock
    private PortefeuilleRepository portefeuilleRepository;


    @Mock
    private CarteVirtuelleRepository carteVirtuelleRepository;

    @InjectMocks
    private CarteVirtuelleService carteVirtuelleService;

    private CarteVirtuelle carteVirtuelle;
    private Portefeuille portefeuille;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        portefeuille = new Portefeuille();
        portefeuille.setSolde(BigDecimal.valueOf(1000));
        portefeuille.setUtilisateurId(1);
        portefeuille.setDevise(Devise.USD);

        when(portefeuilleRepository.save(any(Portefeuille.class))).thenReturn(portefeuille);
        portefeuille = portefeuilleRepository.save(portefeuille);

        carteVirtuelle = new CarteVirtuelle();
        carteVirtuelle.setNumeroCarte(1);
        carteVirtuelle.setSolde(BigDecimal.valueOf(500));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        carteVirtuelle.setDateExpiration(calendar.getTime());
        carteVirtuelle.setCvv(123);
        carteVirtuelle.setPortefeuille(portefeuille);
    }

    @Test
    void testGenererCarte() {
        when(carteVirtuelleRepository.save(any(CarteVirtuelle.class))).thenReturn(carteVirtuelle);

        CarteVirtuelle result = carteVirtuelleService.genererCarte(500);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(500), result.getSolde());
        assertNotNull(result.getDateExpiration());
        assertTrue(result.getCvv() >= 100 && result.getCvv() <= 999);
        verify(carteVirtuelleRepository, times(1)).save(any(CarteVirtuelle.class));
    }

    @Test
    void testActiver() {
        when(carteVirtuelleRepository.save(any(CarteVirtuelle.class))).thenReturn(carteVirtuelle);

        CarteVirtuelle result = carteVirtuelleService.activer(carteVirtuelle);

        assertNotNull(result);
        assertTrue(result.getStatut().equals(StatutCarteVirtuelle.ACTIVE));
        verify(carteVirtuelleRepository, times(1)).save(carteVirtuelle);
    }

    @Test
    void testDesactiver() {
        when(carteVirtuelleRepository.save(any(CarteVirtuelle.class))).thenReturn(carteVirtuelle);

        CarteVirtuelle result = carteVirtuelleService.desactiver(carteVirtuelle);

        assertNotNull(result);
        assertFalse(result.getStatut().equals(StatutCarteVirtuelle.ACTIVE));
        verify(carteVirtuelleRepository, times(1)).save(carteVirtuelle);
    }

    @Test
    void testVerifyExpiration() {
        when(carteVirtuelleRepository.save(any(CarteVirtuelle.class))).thenReturn(carteVirtuelle);

        CarteVirtuelle result = carteVirtuelleService.verify_expiration(carteVirtuelle);

        assertNotNull(result);
        verify(carteVirtuelleRepository, times(1)).save(carteVirtuelle);
    }

    @Test
    void testRetirer() {
        when(carteVirtuelleRepository.save(any(CarteVirtuelle.class))).thenReturn(carteVirtuelle);

        CarteVirtuelle result = carteVirtuelleService.retirer(carteVirtuelle, 100);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(400), result.getSolde());
        assertEquals(BigDecimal.valueOf(900), result.getPortefeuille().getSolde());
        verify(carteVirtuelleRepository, times(1)).save(carteVirtuelle);
    }
    @Test
    void testRetirerMoreThanSolde() {
        when(carteVirtuelleRepository.save(any(CarteVirtuelle.class))).thenReturn(carteVirtuelle);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            carteVirtuelleService.retirer(carteVirtuelle, 1500);
        });

        String expectedMessage = "Solde Insuffisant";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}