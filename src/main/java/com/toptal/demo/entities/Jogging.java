package com.toptal.demo.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.toptal.demo.util.CustomDateDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the jogging database table.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jogging implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.DATE)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date date;

    // the distance to run
    private int distance;

    // the time(period) to run
    private float time;

    /**
     *  weather related info, clouds, temperature, windSpead
     */
    private String weatherDescription;

    private String temperature;

    private String windSpeed;

    // bi-directional many-to-one association to Location
    @ManyToOne
    private Location location;

    @JsonIgnore
    // bi-directional many-to-one association to User
    @ManyToOne
    private User user;
}
