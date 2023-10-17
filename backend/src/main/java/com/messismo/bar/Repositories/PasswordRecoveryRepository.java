package com.messismo.bar.Repositories;

import com.messismo.bar.Entities.PasswordRecovery;
import com.messismo.bar.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long> {
    Optional<PasswordRecovery> findByUser(User user);
}
