package com.toptal.demo.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the location database table.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "location", uniqueConstraints = @UniqueConstraint(name = "uc_locationName", columnNames = { "locationName" }))
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private float latitutde;

    @Column(nullable = true)
    private float longtude;

    @Column(nullable = false)
    private String locationName;

    // bi-directional many-to-one association to Jogging
    @OneToMany(mappedBy = "location")
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
