package client.gestion_portefeuilles.Service;

import client.gestion_portefeuilles.Model.CarteVirtuelle;
import client.gestion_portefeuilles.Model.Devise;
import client.gestion_portefeuilles.Model.Portefeuille;
import client.gestion_portefeuilles.Repository.CarteVirtuelleRepository;
import client.gestion_portefeuilles.Repository.PortefeuilleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PortefeuilleServiceTests {

    @Mock
    private PortefeuilleRepository portefeuilleRepository;

    @Mock
    private CarteVirtuelleRepository carteVirtuelleRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PortefeuilleService portefeuilleService;

    private Portefeuille portefeuille;
    private CarteVirtuelle carteVirtuelle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        portefeuille = new Portefeuille();
        portefeuille.setId(1);
        portefeuille.setSolde(BigDecimal.valueOf(1000));
        portefeuille.setUtilisateurId(3);
        portefeuille.setDevise(Devise.USD);

        carteVirtuelle = new CarteVirtuelle();
        carteVirtuelle.setNumeroCarte(1);
        carteVirtuelle.setSolde(BigDecimal.valueOf(500));
    }

    @Test
    void testCreerPorteFeuille() {
        when(portefeuilleRepository.save(any(Portefeuille.class))).thenReturn(portefeuille);

        Portefeuille result = portefeuilleService.creerPorteFeuille(1, Devise.EUR, BigDecimal.valueOf(1000));

        assertNotNull(result);
        assertEquals(1, result.getUtilisateurId());
        assertEquals(BigDecimal.valueOf(1000), result.getSolde());
        verify(portefeuilleRepository, times(1)).save(any(Portefeuille.class));
    }

    @Test
    void testAjouterCarte() {
        when(portefeuilleRepository.findById(anyInt())).thenReturn(portefeuille);
        when(portefeuilleRepository.save(any(Portefeuille.class))).thenReturn(portefeuille);

        Portefeuille result = portefeuilleService.ajouterCarte(portefeuille.getId(), carteVirtuelle);

        assertNotNull(result);
        assertTrue(result.getCartes().contains(carteVirtuelle));
        verify(portefeuilleRepository, times(1)).save(portefeuille);
    }

    @Test
    void testDeposer() {
        when(portefeuilleRepository.save(any(Portefeuille.class))).thenReturn(portefeuille);

        Portefeuille result = portefeuilleService.deposer(portefeuille, 500);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1500), result.getSolde());
        verify(portefeuilleRepository, times(1)).save(portefeuille);
    }

    @Test
    void testRetirer() {
        when(portefeuilleRepository.save(any(Portefeuille.class))).thenReturn(portefeuille);

        Portefeuille result = portefeuilleService.retirer(portefeuille, 500);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(500), result.getSolde());
        verify(portefeuilleRepository, times(1)).save(portefeuille);
    }

    @Test
    void testGetPortefeuille() {
        when(portefeuilleRepository.getByUtilisateurId(1)).thenReturn(portefeuille);

        Portefeuille result = portefeuilleService.getPortefeuille(1);

        assertNotNull(result);
        assertEquals(3, result.getUtilisateurId());
        verify(portefeuilleRepository, times(1)).getByUtilisateurId(1);
    }

    @Test
    void testGetCartes() {
        List<CarteVirtuelle> cartes = List.of(carteVirtuelle);
        when(portefeuilleRepository.findCarteVirtuellesById(1)).thenReturn(cartes);

        List<CarteVirtuelle> result = portefeuilleService.getCartes(portefeuille);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(carteVirtuelle));
        verify(portefeuilleRepository, times(1)).findCarteVirtuellesById(1);
    }

    @Test
    void testRetirerMoreThanSolde() {
        when(portefeuilleRepository.save(any(Portefeuille.class))).thenReturn(portefeuille);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            portefeuilleService.retirer(portefeuille, 1500);
        });

        String expectedMessage = "Solde Insuffisant";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeposerNullPortefeuille() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            portefeuilleService.deposer(null, 500);
        });

        String expectedMessage = "Portefeuille cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testAjouterCarteNullCarte() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            portefeuilleService.ajouterCarte(1, null);
        });

        String expectedMessage = "Carte cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testConvertir() {
        String jsonResponse = "{ \"base\": \"USD\", \"last_updated\": 1733145300, \"exchange_rates\": { \"EUR\": 0.951294 } }";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(portefeuilleRepository.save(any(Portefeuille.class))).thenReturn(portefeuille);

        Portefeuille result = portefeuilleService.convertir(Devise.EUR, portefeuille);

        assertNotNull(result);
        assertEquals(Devise.EUR, result.getDevise());
        assertEquals(-1, BigDecimal.valueOf(951.294).compareTo(result.getSolde()));
        verify(portefeuilleRepository, times(1)).save(portefeuille);
    }
}