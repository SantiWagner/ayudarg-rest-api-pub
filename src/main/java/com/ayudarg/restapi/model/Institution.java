package com.ayudarg.restapi.model;

import java.sql.Timestamp;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "institutions")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(value="INSTITUTION")
@JsonIdentityInfo(		generator = ObjectIdGenerators.PropertyGenerator.class, 
						property  = "id", 
						scope     = Long.class)
public class Institution extends OwnerEntity {

	@NotBlank
	@Size(max = 150)
	private String name;
	
	@NotBlank
	private String description;
	
	@CreationTimestamp
	private Timestamp created_on;
	
	@UpdateTimestamp
	private Timestamp last_updated_on;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "admin_id", referencedColumnName = "id")
	private User admin;

	public Institution(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
}
