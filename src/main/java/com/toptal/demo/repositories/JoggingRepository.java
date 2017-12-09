package com.toptal.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.toptal.demo.entities.Jogging;

public interface JoggingRepository extends CrudRepository<Jogging, Long> {

    List<Jogging> findByUserEmail(String email);
}
