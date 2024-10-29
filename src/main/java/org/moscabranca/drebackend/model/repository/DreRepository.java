package org.moscabranca.drebackend.model.repository;

import org.moscabranca.drebackend.model.Dre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DreRepository extends JpaRepository<Dre, Long> {
    // Métodos específicos podem ser adicionados aqui, se necessário
}
