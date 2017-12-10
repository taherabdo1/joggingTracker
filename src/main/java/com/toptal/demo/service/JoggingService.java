package com.toptal.demo.service;

import java.util.List;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.JoggingRequestDTO;

public interface JoggingService {

    public JoggingReponseDto addNewJogging(final JoggingRequestDTO joggingRequestDTO, String userEmail) throws ToptalException;

    public JoggingReponseDto getOne(final Long id) throws ToptalException;

    public JoggingReponseDto update(final JoggingRequestDTO joggingRequestDTO) throws ToptalException;

    public List<JoggingReponseDto> getAllForAuser(final String userEmail) throws ToptalException;

    public List<JoggingReponseDto> getAll();

    public void deleteJogging(final Long id) throws ToptalException;

}
