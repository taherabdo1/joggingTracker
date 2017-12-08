package com.toptal.demo.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toptal.demo.entities.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")//, uniqueConstraints = @UniqueConstraint(name = "uc_username", columnNames = { "username" }) )
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	@NotNull(message = "Username can not be null!")
	private String email;

	@JsonIgnore
	@Column(nullable = false)
	@NotNull(message = "Password can not be null!")
	private String password;

	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private Boolean activated = false;

    @Column(nullable = false)
	private boolean isBlocked = false;
	
    @Column(nullable = true)
    private String city;

    //bi-directional one-to-one association to ActivateKey
    @OneToOne(mappedBy="user")
    private ActivateKey activateKey;

    //bi-directional many-to-one association to Jogging
    @OneToMany(mappedBy="user")
    private List<Jogging> joggings;
    
    //bi-directional one-to-one association to loginAttempt
    @OneToOne(mappedBy="user")
    private LoginAttempt loginAttempt;


}

