package com.toptal.demo.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.toptal.demo.entities.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Override
    Page<User> findAll(Pageable pageRequest);

    @Override
    Page<User> findAll(Specification<User> specification, Pageable pageRequest);

    Optional<User> findOneByEmail(String email);

}
