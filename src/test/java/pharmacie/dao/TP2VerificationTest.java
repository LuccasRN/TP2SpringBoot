package pharmacie.dao;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import pharmacie.entity.Categorie;
import pharmacie.entity.Commande;
import pharmacie.entity.Dispensaire;
import pharmacie.entity.Ligne;
import pharmacie.entity.Medicament;

@DataJpaTest
public class TP2VerificationTest {

    @Autowired private TestEntityManager entityManager;
    
    @Autowired private CategorieRepository categorieRepository;
    @Autowired private MedicamentRepository medicamentRepository;
    @Autowired private DispensaireRepository dispensaireRepository;
    @Autowired private CommandeRepository commandeRepository;
    @Autowired private LigneRepository ligneRepository;

    @Test
    void testToutLeTP2() {
        
        
        Categorie c = new Categorie(); 
        c.setLibelle("Categoria Protegida"); 
        c = entityManager.persistAndFlush(c);
        
        Medicament m = new Medicament(); 
        m.setNom("Medicamento Vinculado"); 
        m.setCategorie(c); 
        entityManager.persistAndFlush(m);
        
       
        entityManager.clear(); 

        Categorie cToDelete = categorieRepository.findById(c.getCode()).orElseThrow();

        
        assertThrows(Exception.class, () -> {
            categorieRepository.delete(cToDelete);
            categorieRepository.flush();
        });

       
        entityManager.clear();

        Dispensaire d = new Dispensaire(); 
        d.setCode("D1"); 
        d.setNom("Dispensario Test"); 
        d = entityManager.persistAndFlush(d);
        
        Commande cmd = new Commande(); 
        cmd.setSaisieLe(LocalDate.now()); 
        cmd.setDispensaire(d); 
        cmd = entityManager.persistAndFlush(cmd);
        
        Integer cmdId = cmd.getNumero();
        
        
        entityManager.refresh(d);
        
        dispensaireRepository.delete(d); 
        dispensaireRepository.flush(); 
        assertThat(commandeRepository.findById(cmdId)).isEmpty(); 

        entityManager.clear(); 
        
        Categorie c2 = new Categorie(); 
        c2.setLibelle("Categoria Queries"); 
        c2 = entityManager.persistAndFlush(c2);
        
        Dispensaire d2 = new Dispensaire(); 
        d2.setCode("REQ"); 
        d2.setNom("Dispensario Consultas"); 
        d2 = entityManager.persistAndFlush(d2);
        
        Medicament dispo = new Medicament(); 
        dispo.setNom("Paracetamol"); 
        dispo.setCategorie(c2); 
        dispo.setUnitesEnStock(100); 
        dispo.setUnitesCommandees(50);
        dispo.setIndisponible(false);
        dispo = entityManager.persistAndFlush(dispo);

        Medicament pasDispo = new Medicament(); 
        pasDispo.setNom("Jarabe Agotado"); 
        pasDispo.setCategorie(c2); 
        pasDispo.setUnitesEnStock(10); 
        pasDispo.setUnitesCommandees(20); 
        entityManager.persistAndFlush(pasDispo);

       
        Commande sent = new Commande(); 
        sent.setDispensaire(d2); 
        sent.setSaisieLe(LocalDate.now().minusDays(2)); 
        sent.setEnvoyeeLe(LocalDate.now().minusDays(1)); 
        sent.addLigne(new Ligne(null, 50, sent, dispo)); 
        entityManager.persistAndFlush(sent);

        Commande ongoing = new Commande(); 
        ongoing.setDispensaire(d2); 
        ongoing.setSaisieLe(LocalDate.now()); 
        ongoing.setEnvoyeeLe(null); 
        entityManager.persistAndFlush(ongoing);
        
        entityManager.clear(); 
        assertThat(ligneRepository.countArticlesEnvoyesByDispensaire("REQ")).isEqualTo(50L);

        assertThat(commandeRepository.findByDispensaireCodeAndEnvoyeeLeIsNull("REQ")).hasSize(1);

        Integer catId = categorieRepository.findByLibelle("Categoria Queries").getCode();
        List<Medicament> meds = medicamentRepository.findDisponiblesByCategorie(catId);
        
        assertThat(meds).hasSize(1);
        assertThat(meds.get(0).getNom()).isEqualTo("Paracetamol");
    }
}