package client.gestion_portefeuilles.controller;

import client.gestion_portefeuilles.Model.Devise;
import client.gestion_portefeuilles.Model.Portefeuille;
import client.gestion_portefeuilles.Repository.PortefeuilleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PortefeuilleControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PortefeuilleRepository portefeuilleRepository;

    private Portefeuille portefeuille;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @BeforeEach
    void setUp() {
        portefeuille = new Portefeuille();
        portefeuille.setUtilisateurId(1);
        portefeuille.setSolde(BigDecimal.valueOf(1000));
        portefeuille.setDevise(Devise.USD);
        portefeuilleRepository.save(portefeuille);
    }

    @Test
    @Order(1)
    void testCreatePortefeuille() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gestion_portefeuille/portefeuille/creer")
                        .param("utilisateurId", "2")
                        .param("solde", "500")
                        .param("devise", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.utilisateurId").value(2))
                .andExpect(jsonPath("$.solde").value(500))
                .andExpect(jsonPath("$.devise").value("EUR"));
    }

    @Test
    @Order(2)
    void testDepositingMoney() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gestion_portefeuille/portefeuille/deposer")
                        .param("somme", "200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(portefeuille)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(1200));
    }

    @Test
    @Order(3)
    void testWithdrawingMoney() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/gestion_portefeuille/portefeuille/retirer")
                        .param("somme", "200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(portefeuille)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(800));
    }

}
