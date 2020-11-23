package com.ayudarg.restapi.model;

import java.sql.Timestamp;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ayudarg.restapi.model.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIdentityInfo(		generator = ObjectIdGenerators.PropertyGenerator.class, 
						property  = "id", 
						scope     = Long.class)
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "resource_id", referencedColumnName = "id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Resource resource;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private TransactionStatus status;
	
	@OneToOne(cascade=CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "transaction_review_id", referencedColumnName = "id")
	private TransactionReview review;
	
	@CreationTimestamp
	private Timestamp created_on;
	
	@UpdateTimestamp
	private Timestamp last_updated_on;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="demand_id", nullable=true)
	private Demand demand;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="donation_id")
	@JsonIgnore
	private Donation donation;
	
	@Column(length=500)
	private String comment;
	
}
