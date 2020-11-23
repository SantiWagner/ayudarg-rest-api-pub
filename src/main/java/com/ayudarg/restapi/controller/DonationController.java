package com.ayudarg.restapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayudarg.restapi.model.Donation;
import com.ayudarg.restapi.model.Institution;
import com.ayudarg.restapi.model.Resource;
import com.ayudarg.restapi.model.Transaction;
import com.ayudarg.restapi.model.User;
import com.ayudarg.restapi.model.enums.DonationStatus;
import com.ayudarg.restapi.model.enums.ResourceStatus;
import com.ayudarg.restapi.model.enums.TransactionStatus;
import com.ayudarg.restapi.payload.request.DonationRequest;
import com.ayudarg.restapi.payload.request.TransactionRequest;
import com.ayudarg.restapi.repository.DonationRepository;
import com.ayudarg.restapi.repository.InstitutionRepository;
import com.ayudarg.restapi.repository.ResourceRepository;
import com.ayudarg.restapi.repository.TransactionRepository;
import com.ayudarg.restapi.repository.UserRepository;
import com.ayudarg.restapi.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/donations")
public class DonationController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ResourceRepository resourceRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	DonationRepository donationRepository;
	
	@PostMapping("/donate")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> donate(@RequestBody DonationRequest payload){
		
		try {
			
			UserDetailsImpl requestUser = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Donation toAdd = new Donation();
			
			Optional<User> donor = userRepository.findById(requestUser.getId());
			
			Optional<Institution> receiver = institutionRepository.findById(payload.getReceiver_id());
			
			//Si no encuentro donante o receptor, entrego NOT_FOUND
			if(donor.isEmpty() || receiver.isEmpty()) 
				return ResponseEntity.notFound().build();
			
			toAdd.setDonor(donor.get());
			toAdd.setReceiver(receiver.get());
			
			List<Transaction> transactions = new ArrayList<>();
			List<Resource> parents = new ArrayList<>();
			
			for(TransactionRequest t : payload.getTransactions()) {
				Optional<Resource> toDonate = resourceRepository.findById(t.getResource_id());
				
				//Si el usuario no es dueño del recurso que se desea donar, entrego BAD_REQUEST
				//Si el recurso no existe, este if arrojará un exception en toDonate.get() el cual será capturado
				if(!toDonate.get().getOwner().getId().equals(donor.get().getId()))
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("recurso no encontrado");
				
				//Creo un objeto Transaction por cada TransactionRequest
				//Debo validar que la cantidad a donar esté disponible
				
				Transaction transaction = new Transaction();
				
				transaction.setComment(t.getComment());
				
				//Si no hay cantidad disponible para donar, aborto la operación con un BAD_REQUEST
				if(toDonate.get().getAmount()<t.getAmount())
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cantidad insuficiente: "+toDonate.get().getAmount());
				
				//Creo un nuevo recurso
				Resource newResource = new Resource();
				newResource.setOwner(toDonate.get().getOwner());
				newResource.setMeasurement_unit(toDonate.get().getMeasurement_unit());
				newResource.setParent(toDonate.get());
				newResource.setAmount(t.getAmount());
				newResource.setStatus(ResourceStatus.COMMITTED);
				newResource.setName(toDonate.get().getName());
				
				
				//Actualizo la cantidad del recurso padre
				toDonate.get().setAmount(toDonate.get().getAmount()-t.getAmount());

				transaction.setStatus(TransactionStatus.PENDING);
				transaction.setResource(newResource);
				transaction.setDonation(toAdd);
				
				toAdd.addTransaction(transaction);
				
				parents.add(toDonate.get());
			}
			
			for(Resource r : parents) {
				resourceRepository.save(r);
			}
			
			//toAdd.setStatus(DonationStatus.PENDING);
			
			return ResponseEntity.ok(donationRepository.save(toAdd));
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/quick_donate")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Donation> quickDonate(@RequestBody Donation payload){
		//Este seguro no lo vamos a usar porque es igual al donate la construcción del payload 
		//se hace en el frontend
		return null;
	}
	
	@GetMapping("/details/{id}")
	@PreAuthorize("isAuthenticated() and hasRole('USER')")
	public ResponseEntity<?> details(@PathVariable Long id){
		
		try {
			Optional<Donation> toReturn = donationRepository.findById(id);
			
			if(toReturn.isPresent())
				return ResponseEntity.ok(toReturn.get());
			else
				return ResponseEntity.notFound().build();
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/accept/{id}")
	@PreAuthorize("isAuthenticated() and hasRole('USER')")
	public ResponseEntity<?> accept(@PathVariable Long id){
		
		return null;
	}
	
	@GetMapping("/cancel/{id}")
	@PreAuthorize("isAuthenticated() and hasRole('USER')")
	public ResponseEntity<?> cancel(@PathVariable Long id){
		
		return null;
	}
	
	@GetMapping("/reject/{id}")
	@PreAuthorize("isAuthenticated() and hasRole('USER')")
	public ResponseEntity<?> reject(@PathVariable Long id){
		
		return null;
	}
	
	@GetMapping("/confirm/{id}")
	@PreAuthorize("isAuthenticated() and hasRole('USER')")
	public ResponseEntity<?> confirm(@PathVariable Long id){
		
		return null;
	}
	
}
