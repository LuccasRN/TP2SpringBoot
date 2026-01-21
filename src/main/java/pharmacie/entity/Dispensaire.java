package pharmacie.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Dispensaire {

    @Id
    @Size(max = 5)
    private String code;

    @NotBlank
    @Size(max = 40)
    private String nom;

    @Size(max = 30)
    private String contact;

    @Size(max = 30)
    private String fonction;

    @Size(max = 24)
    private String telephone;

    @Size(max = 24)
    private String fax;

    // Intégration de l'adresse (flattening dans la table DISPENSAIRE)
    @Embedded
    private AdressePostale adresse;

    // Relation inverse (optionnelle mais utile pour la navigation)
    // Un dispensaire peut avoir plusieurs commandes
    @OneToMany(mappedBy = "dispensaire", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Commande> commandes = new ArrayList<>();
}