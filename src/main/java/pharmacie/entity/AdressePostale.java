package pharmacie.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdressePostale {

    @NotBlank
    @Size(max = 60)
    private String adresse;

    @NotBlank
    @Size(max = 15)
    private String ville;

    @NotBlank
    @Size(max = 15)
    private String region;

    @NotBlank
    @Size(max = 10)
    private String codePostal;

    @NotBlank
    @Size(max = 15)
    private String pays;
}
