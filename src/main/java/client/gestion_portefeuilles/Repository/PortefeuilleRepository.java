package client.gestion_portefeuilles.Repository;

import client.gestion_portefeuilles.Model.CarteVirtuelle;
import client.gestion_portefeuilles.Model.Portefeuille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortefeuilleRepository extends JpaRepository<Portefeuille, Integer> {
    Portefeuille getByUtilisateurId(int id);
    @Query(value = "SELECT c FROM CarteVirtuelle c WHERE c.portefeuille.id = :portefeuilleId")
    List<CarteVirtuelle> findCarteVirtuellesById(int portefeuilleId);
    @Query(value = "SELECT p FROM Portefeuille p WHERE p.id = :id")
    Portefeuille findById(int id);

}
