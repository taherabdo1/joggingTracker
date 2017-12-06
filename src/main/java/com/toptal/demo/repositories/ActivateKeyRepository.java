package com.toptal.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.toptal.demo.entities.ActivateKey;

public interface ActivateKeyRepository  extends CrudRepository<ActivateKey, Long>
{

    public ActivateKey findByKeySerial(String keySerial);
}
