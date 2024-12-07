package client.gestion_portefeuilles.Repository;

import client.gestion_portefeuilles.Model.CarteVirtuelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteVirtuelleRepository extends JpaRepository<CarteVirtuelle,Integer> {
    CarteVirtuelle findByNumeroCarte(int numeroCarte);
}
