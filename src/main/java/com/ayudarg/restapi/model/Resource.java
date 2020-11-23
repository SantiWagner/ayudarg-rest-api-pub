package com.ayudarg.restapi.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.ayudarg.restapi.model.enums.MeasurementUnit;
import com.ayudarg.restapi.model.enums.ResourceStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resources")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(		generator = ObjectIdGenerators.PropertyGenerator.class, 
						property  = "id", 
						scope     = Long.class)
public class Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;
	
	@NotBlank
	private Double amount;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name = "parent_id", referencedColumnName = "id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Resource parent;  

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = {CascadeType.ALL})
	@JsonIgnore
	protected Set<Resource> children;  
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", referencedColumnName = "id", nullable=false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private OwnerEntity owner;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private MeasurementUnit measurement_unit;
	
	@Enumerated(EnumType.STRING)
	@Column
	private ResourceStatus status;
	
}
