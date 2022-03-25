package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByUsersId(Long id);
    UserDetails findByUsersEmail(String email);
    Page<UserDetails> findAllByDoctorCodeIdIsNotNull(Pageable pageable);
}
