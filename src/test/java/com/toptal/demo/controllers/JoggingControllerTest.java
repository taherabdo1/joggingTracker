package com.toptal.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.JoggingRequestDTO;
import com.toptal.demo.dto.LocationDto;
import com.toptal.demo.dto.UpdateJogDto;
import com.toptal.demo.service.JoggingService;

@RunWith(MockitoJUnitRunner.class)
public class JoggingControllerTest {

    @Mock
    JoggingService joggingService;

    @InjectMocks
    JoggingController joggingController;

    @Rule
    public ExpectedException thrown = none();


    final LocationDto locationDto = new LocationDto(123.2f, 123.3f, "cairo");

    final JoggingRequestDTO joggingRequestDTO = new JoggingRequestDTO(new Date(), 45, 4000, locationDto);

    final JoggingReponseDto joggingReponseDto = new JoggingReponseDto(1L, new Date(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", locationDto, 5.5);

    final UpdateJogDto updateJogDto = new UpdateJogDto(1l, new Date(), 50, 1000, locationDto);
    final String EMAIL = "test@demo.com";

    @Test
    public void testGetOne() throws ToptalException {
        doReturn(joggingReponseDto).when(joggingService).getOne(1L);
        final JoggingReponseDto response = joggingController.getOne(1L);
        assertEquals(response.getDistance(), joggingReponseDto.getDistance());

    }

    @Test
    public void testGetOneWithToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(joggingService).getOne(anyLong());
        joggingController.getOne(1L);

    }

    @Test
    public void testUpdate() throws ToptalException {
        doReturn(joggingReponseDto).when(joggingService).update(updateJogDto);
        final JoggingReponseDto response = joggingController.update(updateJogDto);
        assertEquals(response.getId().longValue(), joggingReponseDto.getId().longValue());

    }

    @Test
    public void testUpdateWithToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(joggingService).update(updateJogDto);
        joggingController.update(updateJogDto);
    }

    @Test
    public void testGetAllForAuser() throws ToptalException{
        final List<JoggingReponseDto> jogs = buildJogsResponseList();
        doReturn(jogs).when(joggingService).getAllForAuser(EMAIL, 0, 10, "");
        final List<JoggingReponseDto> response = joggingController.getAllForAuser(EMAIL, 0, 10, "");
        assertEquals(response.size(), jogs.size());
    }

    @Test
    public void testGetAllForAuserWithToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(joggingService).getAllForAuser(EMAIL, 0, 10, "");
        joggingController.getAllForAuser(EMAIL, 0, 10, "");
    }

    @Test
    public void testDeleteJog() throws ToptalException {
        doReturn(true).when(joggingService).deleteJogging(1L);
        final Response response = joggingController.deleteJogging(1L);
        assertEquals(response.getStatus(), 200);
    }

    @Test
    public void testDeleteJogWithToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(joggingService).deleteJogging(1L);
        joggingController.deleteJogging(1L);

    }

    private List<JoggingReponseDto> buildJogsResponseList() {
        final List<JoggingReponseDto> jogs = new ArrayList<>();
        jogs.add(new JoggingReponseDto(1L, new Date(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", locationDto, 5.5));
        jogs.add(new JoggingReponseDto(2L, new Date(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", locationDto, 5.5));
        jogs.add(new JoggingReponseDto(3L, new Date(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", locationDto, 5.5));
        return jogs;
    }
}
