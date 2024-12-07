package client.gestion_portefeuilles.Model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class CarteVirtuelle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int numeroCarte;
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    private BigDecimal solde;
    private int cvv;
    @Enumerated(EnumType.STRING)
    private StatutCarteVirtuelle statut = StatutCarteVirtuelle.ACTIVE;
    @ManyToOne()
    @JoinColumn(name = "portefeuille_id")
    private Portefeuille portefeuille;

    public void activer(){
        this.statut= StatutCarteVirtuelle.ACTIVE;
    }
    public void desactiver(){
        this.statut= StatutCarteVirtuelle.INACTIVE;
    }
    public void verify_expiration(){
        if (new Date().after(this.getDateExpiration())){
            this.statut = StatutCarteVirtuelle.EXPIREE;
        }
    }
    public void deposer(BigDecimal somme){
        this.solde = this.solde.add(somme);
    }
    public void retirer(BigDecimal somme) {
        if (this.solde.compareTo(somme) >= 0) {
            this.solde = this.solde.subtract(somme);
        } else {
            throw new RuntimeException("Solde Insuffisant");
        }
        if (this.portefeuille != null) {
            this.portefeuille.retirer(somme);
        }
    }
}
