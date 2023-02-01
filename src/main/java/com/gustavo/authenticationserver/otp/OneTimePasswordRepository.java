package com.gustavo.authenticationserver.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, String> {
    Optional<OneTimePassword> findOneTimePasswordByUsername(String username);
}
