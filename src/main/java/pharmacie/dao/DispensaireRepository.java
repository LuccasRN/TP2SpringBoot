package pharmacie.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Dispensaire;

public interface DispensaireRepository extends JpaRepository<Dispensaire, String> {
    
    // Trouver tous les dispensaires dans une région donnée
    // Spring Data navigue dans l'objet imbriqué 'adresse' pour trouver le champ 'region'
    List<Dispensaire> findByAdresseRegion(String region);
}