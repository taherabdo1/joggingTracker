package com.toptal.demo.service;

import java.util.List;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.JoggingRequestDTO;
import com.toptal.demo.dto.UpdateJogDto;

public interface JoggingService {

    public JoggingReponseDto addNewJogging(final JoggingRequestDTO joggingRequestDTO, String userEmail) throws ToptalException;

    public JoggingReponseDto getOne(final Long id) throws ToptalException;

    public JoggingReponseDto update(final UpdateJogDto updateJogDto, final String email) throws ToptalException;

    public List<JoggingReponseDto> getAllForAuser(final String userEmail, final int pageNumer, final int pageSize, final String filterBy)
        throws ToptalException;

    public List<JoggingReponseDto> getAll(final int pageNumber, final int pageSize, final String filterBy) throws ToptalException;

    public boolean deleteJogging(final Long id) throws ToptalException;

}
