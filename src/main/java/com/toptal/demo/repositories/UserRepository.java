package com.toptal.demo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.toptal.demo.entities.User;

public interface UserRepository  extends CrudRepository<User, Long>
{

    Optional<User> findOneByEmail(String email);

}
