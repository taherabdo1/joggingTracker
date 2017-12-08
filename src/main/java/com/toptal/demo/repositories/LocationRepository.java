package com.toptal.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.toptal.demo.entities.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {

    public Location findByLocationName(String locationName);

}
