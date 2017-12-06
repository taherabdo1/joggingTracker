package com.toptal.demo.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the activate_key database table.
 * 
 */
@Entity
@Table(name="login_attempt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempt {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    private Date date;

    
    //bi-directional one-to-one association to User
    @JsonIgnore
    @OneToOne
    private User user;
    
    private int numberOfTrials;
}
