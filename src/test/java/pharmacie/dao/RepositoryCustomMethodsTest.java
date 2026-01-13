package pharmacie.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pharmacie.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RepositoryCustomMethodsTest {

    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private MedicamentRepository medicamentRepository;
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private DispensaireRepository dispensaireRepository;    


    @Test // Ce test se base uniquement sur les données définies dans data.sql
    public void testMedicamentCustomMethods() {    
        Medicament indisponible = medicamentRepository.findByNom("Lévofloxacine 500mg").orElseThrow();
        Medicament disponible   = medicamentRepository.findByNom("Doliprane Effervescent 1g").orElseThrow();
    
        // Trouve tous les médicaments disponibles
        List<Medicament> disponibles = medicamentRepository.findByIndisponibleFalse();

        assertTrue(disponibles.contains(disponible));
        assertFalse(disponibles.contains(indisponible));        
        assertFalse(disponibles.isEmpty());
    }

    @Test // Ce test crée les enregistrements nécessaires
    public void testCategorieCustomMethods() {
        Categorie c1 = new Categorie();
        c1.setLibelle("AnalgesiquesTest");
        categorieRepository.save(c1);

        Categorie c2 = new Categorie();
        c2.setLibelle("AntibiotiquesTest");
        categorieRepository.save(c2);

        // findByLibelle
        Categorie found = categorieRepository.findByLibelle("AnalgesiquesTest");
        assertNotNull(found);
        assertEquals("AnalgesiquesTest", found.getLibelle());

        // findByLibelleContaining
        List<Categorie> list = categorieRepository.findByLibelleContaining("iquesTest");
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AntibiotiquesTest")));
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AnalgesiquesTest")));
    }

    @Test
    void testFindCommandesAfterDate() {
        // Given (Données chargées via data.sql : une commande le 2025-01-10 et une le 2023-05-20)
        LocalDate datePivot = LocalDate.of(2024, 1, 1);

        // When
        List<Commande> results = commandeRepository.findBySaisieLeAfter(datePivot);

        // Then
        assertThat(results).isNotEmpty();
        // On s'attend à trouver celle de 2025, mais pas celle de 2023
        assertThat(results).anyMatch(c -> c.getSaisieLe().isAfter(datePivot));
        assertThat(results).noneMatch(c -> c.getSaisieLe().isBefore(datePivot));
    }

    @Test
    void testFindDispensairesByRegion() {
        // Given (Données chargées via data.sql : Occitanie)
        String regionRecherchee = "Occitanie";

        // When
        List<Dispensaire> results = dispensaireRepository.findByAdresseRegion(regionRecherchee);

        // Then
        assertThat(results).isNotEmpty();
        assertThat(results.size()).isGreaterThanOrEqualTo(2); // Nous avons inséré 2 dispensaires en Occitanie
        assertThat(results.get(0).getAdresse().getRegion()).isEqualTo(regionRecherchee);
    }


}
