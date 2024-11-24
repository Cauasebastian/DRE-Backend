package org.moscabranca.drebackend.repository;

import org.moscabranca.drebackend.model.DreAnual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DreAnualRepository extends JpaRepository<DreAnual, Long> {
}
