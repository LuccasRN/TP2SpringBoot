package pharmacie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "dispensaire")
    @ToString.Exclude
    private List<Commande> commandes = new ArrayList<>();
}