package pharmacie.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pharmacie.entity.Ligne;

public interface LigneRepository extends JpaRepository<Ligne, Integer> {
    @Query("SELECT SUM(l.quantite) FROM Ligne l WHERE l.commande.dispensaire.code = :codeDispensaire AND l.commande.envoyeeLe IS NOT NULL")
    Long countArticlesEnvoyesByDispensaire(@Param("codeDispensaire") String codeDispensaire);
}
