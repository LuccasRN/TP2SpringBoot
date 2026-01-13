package pharmacie.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Commande;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {

    // Trouver toutes les commandes saisies après une date donnée
    List<Commande> findBySaisieLeAfter(LocalDate date);
}
