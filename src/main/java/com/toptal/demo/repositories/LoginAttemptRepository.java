package com.toptal.demo.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.toptal.demo.entities.LoginAttempt;

public interface LoginAttemptRepository extends CrudRepository<LoginAttempt, Long> {

    @Modifying
    @Query(value = "UPDATE login_attempt t, user u set t.number_of_trials = t.number_of_trials + 1 WHERE t.user_id = u.id and u.email = :email", nativeQuery = true)
    public void updateLoginAttempIncreamentTrialsCount(@Param(value = "email") final String email);

    public LoginAttempt findByUserEmail(String email);
}
