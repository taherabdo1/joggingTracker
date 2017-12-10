package com.toptal.demo.repositories;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.toptal.demo.entities.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>
{

    Optional<User> findOneByEmail(String email);

}
