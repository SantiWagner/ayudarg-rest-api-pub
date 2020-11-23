package com.ayudarg.restapi.model;

import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "entities")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter
@Setter
@JsonIdentityInfo(		generator = ObjectIdGenerators.PropertyGenerator.class, 
						property  = "id", 
						scope     = Long.class)
public class OwnerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@JsonIgnore
	@OneToMany(mappedBy="owner", fetch = FetchType.LAZY)
	protected Set<Resource> resources;
	
	@JsonIgnore
	@OneToMany(mappedBy="donor", fetch = FetchType.LAZY)
	protected Set<Donation> donations;
	
	@JsonIgnore
	@OneToMany(mappedBy="receiver", fetch = FetchType.LAZY)
	protected Set<Donation> receptions;

}

