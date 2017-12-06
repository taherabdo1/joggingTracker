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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//	@Column(nullable = true)
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//	private ZonedDateTime dateCreated = ZonedDateTime.now();

	@Column(nullable = false)
	@NotNull(message = "Username can not be null!")
	private String email;

	@Column(nullable = false)
	@NotNull(message = "Password can not be null!")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private Boolean active = false;

	

    //bi-directional many-to-one association to ActivateKey
    @OneToMany(mappedBy="user")
    private List<ActivateKey> activateKeys;

    //bi-directional many-to-one association to Jogging
    @OneToMany(mappedBy="user")
    private List<Jogging> joggings;
}

