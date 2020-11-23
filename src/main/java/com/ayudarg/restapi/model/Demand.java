package com.ayudarg.restapi.model;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ayudarg.restapi.model.enums.DemandStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "demands")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(		generator = ObjectIdGenerators.PropertyGenerator.class, 
					property  = "id", 
					scope     = Long.class)
public class Demand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;
	
	private DemandStatus status;
	
	private String status_comment;
	
	@CreationTimestamp
	private Timestamp created_on;
	
	@UpdateTimestamp
	private Timestamp last_updated_on;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="project_id", nullable=false)
	private Project project;
	
	@OneToMany(mappedBy="demand", fetch = FetchType.LAZY)
    private Set<Transaction> transactions;
}
