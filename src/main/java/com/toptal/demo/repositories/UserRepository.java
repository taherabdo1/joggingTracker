package com.toptal.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.toptal.demo.entities.User;

public interface UserRepository extends CrudRepository<User, Long>
{

    List<User> findAll(Pageable pageRequest);
    Optional<User> findOneByEmail(String email);

}
