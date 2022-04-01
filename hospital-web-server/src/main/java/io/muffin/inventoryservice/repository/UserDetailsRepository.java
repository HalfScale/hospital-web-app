package io.muffin.inventoryservice.repository;

import io.muffin.inventoryservice.model.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByUsersId(Long id);

    UserDetails findByUsersEmail(String email);

    Page<UserDetails> findAllByDoctorCodeIdIsNotNull(Pageable pageable);

    @Query("SELECT user FROM UserDetails user WHERE " +
            "(user.firstName LIKE %?1% OR user.lastName LIKE %?1%) AND " +
            "(user.doctorCodeId IS NOT NULL AND user.doctorCodeId LIKE %?2%)")
    Page<UserDetails> findByName(String name, String doctorCode, Pageable pageable);

    @Query("SELECT user FROM UserDetails user WHERE " +
            "(user.firstName LIKE %?1% AND user.lastName LIKE %?2%) AND" +
            "(user.doctorCodeId IS NOT NULL AND user.doctorCodeId LIKE %?3%)")
    Page<UserDetails> findByFullName(String firstName, String lastName, String doctorCode, Pageable pageable);

}
