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

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // the period to run in minutes
    int periodInMinutes;
    
    // the distance to run
    private int distance;

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
