package com.toptal.demo.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the location database table.
 * 
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private float latitutde;

	private String locationName;

	private float longtude;

	//bi-directional many-to-one association to Jogging
	@OneToMany(mappedBy="location")
	private List<Jogging> joggings;

	public Jogging addJogging(final Jogging jogging) {
		getJoggings().add(jogging);
		jogging.setLocation(this);

		return jogging;
	}

	public Jogging removeJogging(final Jogging jogging) {
		getJoggings().remove(jogging);
		jogging.setLocation(null);

		return jogging;
	}

}