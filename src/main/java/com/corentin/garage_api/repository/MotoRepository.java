package com.corentin.garage_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.corentin.garage_api.entity.Moto;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {
    List<Moto> findByMarque(String marque);
}
