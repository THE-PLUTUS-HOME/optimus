package com.theplutushome.optimus.repository;

import com.theplutushome.optimus.dto.UserData;
import com.theplutushome.optimus.entity.User;
import com.theplutushome.optimus.entity.enums.UserAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndDeleted(String username, boolean deleted);
    Optional<User> findByEmailAndUserAccountStatus(String email, UserAccountStatus userAccountStatus);
    Optional<User> findByEmailAndDeleted(String email, boolean deleted);
    Optional<User> findByReferralCodeAndDeleted(String referralCode, boolean deleted);

}
