package client.gestion_portefeuilles.controller;


import client.gestion_portefeuilles.Model.CarteVirtuelle;
import client.gestion_portefeuilles.Model.StatutCarteVirtuelle;
import client.gestion_portefeuilles.Repository.CarteVirtuelleRepository;
import client.gestion_portefeuilles.Service.CarteVirtuelleService;
import client.gestion_portefeuilles.Service.PortefeuilleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CarteVirtuelleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private CarteVirtuelleService carteVirtuelleService;

    @Autowired
    private CarteVirtuelleRepository carteVirtuelleRepository;

    @Autowired
    private PortefeuilleService portefeuilleService;

    private CarteVirtuelle carteVirtuelle;


    @BeforeEach
    void setUp() {
        carteVirtuelle = carteVirtuelleService.genererCarte(200);
        portefeuilleService.ajouterCarte(52,carteVirtuelle);
    }

    @Test
    @Order(1)
    void testGenererCarte() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gestion_portefeuille/carte_virtuelle/generer")
                        .param("somme", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(200));
    }

    @Test
    @Order(2)
    void testActiverCarte() throws Exception {
        CarteVirtuelle latestCarteVirtuelle = carteVirtuelleRepository.findByNumeroCarte(carteVirtuelle.getNumeroCarte());
        mockMvc.perform(MockMvcRequestBuilders.post("/gestion_portefeuille/carte_virtuelle/activer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(latestCarteVirtuelle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut", equalTo(StatutCarteVirtuelle.ACTIVE.toString().trim())));
    }

    @Test
    @Order(3)
    void testDesactiverCarte() throws Exception {
        CarteVirtuelle latestCarteVirtuelle = carteVirtuelleRepository.findByNumeroCarte(carteVirtuelle.getNumeroCarte());
        mockMvc.perform(MockMvcRequestBuilders.post("/gestion_portefeuille/carte_virtuelle/desactiver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(latestCarteVirtuelle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut", equalTo(StatutCarteVirtuelle.INACTIVE.toString().trim())));
    }

    @Test
    @Order(4)
    void testVerifyExpiration() throws Exception {
        CarteVirtuelle latestCarteVirtuelle = carteVirtuelleRepository.findByNumeroCarte(carteVirtuelle.getNumeroCarte());
        mockMvc.perform(MockMvcRequestBuilders.post("/gestion_portefeuille/carte_virtuelle/verify_expiration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(latestCarteVirtuelle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut", equalTo(StatutCarteVirtuelle.ACTIVE.toString().trim())));
    }

    @Test
    @Order(5)
    void testRetirerCarte() throws Exception {
        CarteVirtuelle latestCarteVirtuelle = carteVirtuelleRepository.findByNumeroCarte(carteVirtuelle.getNumeroCarte());
        mockMvc.perform(MockMvcRequestBuilders.post("/gestion_portefeuille/carte_virtuelle/retirer")
                        .param("somme", "50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(latestCarteVirtuelle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(150));
    }
}