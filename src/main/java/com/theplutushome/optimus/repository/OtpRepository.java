package com.theplutushome.optimus.repository;

import com.theplutushome.optimus.entity.OTPRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTPRequest, Integer> {
    Optional<OTPRequest> findByEmail(String email);
}
