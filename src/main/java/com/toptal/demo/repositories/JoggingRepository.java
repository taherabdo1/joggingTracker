package com.toptal.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.toptal.demo.entities.Jogging;

public interface JoggingRepository extends PagingAndSortingRepository<Jogging, Long> {

//    @Query(nativeQuery = true, value = "Select From Jogging where j.email = :email")
    List<Jogging> findByUserEmail(@Param("email") String email, Pageable pageReguest);

}
