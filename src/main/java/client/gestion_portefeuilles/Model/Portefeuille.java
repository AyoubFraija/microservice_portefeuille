package client.gestion_portefeuilles.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Portefeuille {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int utilisateurId;
    private BigDecimal solde;
    @Enumerated(EnumType.STRING)
    private Devise devise;
    @JsonIgnore
    @OneToMany(mappedBy = "portefeuille", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CarteVirtuelle> cartes = new ArrayList<>();

    public void deposer(BigDecimal somme){
        this.solde = this.solde.add(somme);
    }
    public void retirer(BigDecimal somme){
        if(this.solde.compareTo(somme)>=0){
            this.solde =this.solde.subtract(somme);
        }
        else{
            throw new RuntimeException("Solde Insuffisant");
        }
    }
    public void ajouterCarte(CarteVirtuelle carte){
        this.cartes.add(carte);
        carte.setPortefeuille(this);
    }
    public void convertir(Devise target,Double rate){
        this.devise = target;
        this.solde = this.solde.multiply(BigDecimal.valueOf(rate));
    }

}
