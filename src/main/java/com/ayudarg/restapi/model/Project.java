package com.ayudarg.restapi.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(		generator = ObjectIdGenerators.PropertyGenerator.class, 
				property  = "id", 
				scope     = Long.class)
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String description;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "institution_id", referencedColumnName = "id")
	private Institution institution;
	
	@OneToMany(mappedBy="project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Demand> demands;
}
