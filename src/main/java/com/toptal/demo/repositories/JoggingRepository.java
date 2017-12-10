package com.toptal.demo.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.toptal.demo.entities.Jogging;

public interface JoggingRepository extends PagingAndSortingRepository<Jogging, Long> {

    List<Jogging> findByUserEmail(String email);
}
