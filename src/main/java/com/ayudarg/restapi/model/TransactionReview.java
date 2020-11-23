package com.ayudarg.restapi.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction_reviews")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String title;
	
	private String comment;
	
	@NotNull
	private double rating;
	
	@CreationTimestamp
	private Timestamp created_on;
	
	@UpdateTimestamp
	private Timestamp last_updated_on;
}
