package com.toptal.demo.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the activate_key database table.
 * 
 */
@Entity
@Table(name="activate_key")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String keySerial;

	//bi-directional many-to-one association to User
	@ManyToOne
	private User user;


}