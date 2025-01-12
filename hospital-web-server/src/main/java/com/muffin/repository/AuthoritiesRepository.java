package com.muffin.repository;

import com.muffin.model.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
    Optional<Authorities> findByName(String name);
}
