package pharmacie.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numero;

    @NotNull
    @PastOrPresent
    private LocalDate saisieLe;

    private LocalDate envoyeeLe;

    private BigDecimal port;

    private String destinataire;

    private BigDecimal remise;

    // L'adresse de livraison
    @Embedded
    private AdressePostale adresseLivraison;

    // Relation ManyToOne vers Dispensaire
    @ManyToOne(optional = false)
    @JoinColumn(name = "dispensaire_code")
    private Dispensaire dispensaire;

    // Composition : Si on détruit la commande, on détruit ses lignes (Cascade + OrphanRemoval)
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Ligne> lignes = new ArrayList<>();
    
    // Méthode utilitaire pour ajouter une ligne
    public void addLigne(Ligne ligne) {
        lignes.add(ligne);
        ligne.setCommande(this);
    }
}