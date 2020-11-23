package com.ayudarg.restapi.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import com.ayudarg.restapi.model.enums.DonationStatus;
import com.ayudarg.restapi.model.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "donations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(		generator = ObjectIdGenerators.PropertyGenerator.class, 
						property  = "id", 
						scope     = Long.class)
public class Donation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "donor_id", referencedColumnName = "id")
	private OwnerEntity donor;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "receiver_id", referencedColumnName = "id")
	private OwnerEntity receiver;

	@CreationTimestamp
	private Timestamp created_on;
	
	@UpdateTimestamp
	private Timestamp last_updated_on;
	
	//(EnumType.STRING)
	//@Column(length = 20)
	//@Transient
	//@Enumerated(EnumType.STRING)
	//private DonationStatus status;
	
	@OneToMany(mappedBy="donation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Set<Transaction> transactions;
	 
	public void addTransaction(Transaction t) {
		 if(this.transactions == null)
			 transactions = new HashSet<>();
		 this.transactions.add(t);
	}
	 	 
	@JsonProperty(value = "status")
	public DonationStatus getStatus() {
		 
		 boolean has_rejected = false;
		 boolean has_accepted = false;
		 
		 for(Transaction t : transactions) {
			 if (t.getStatus() == TransactionStatus.PENDING)
				 return DonationStatus.PENDING;
			 
			 if(t.getStatus() == TransactionStatus.REJECTED)
				 has_rejected = true;
			 
			 if(t.getStatus() == TransactionStatus.ACCEPTED)
				 has_accepted = true;
		 }
		 
		 if(has_accepted && has_rejected)
			 return DonationStatus.PARTIALLY_ACCEPTED;
		 
		 if(has_accepted && !(has_rejected))
			 return DonationStatus.ACCEPTED;
		 
		 if(has_rejected && !(has_accepted))
			 return DonationStatus.REJECTED;
		 
		 return DonationStatus.CANCELED;
	 }
}
