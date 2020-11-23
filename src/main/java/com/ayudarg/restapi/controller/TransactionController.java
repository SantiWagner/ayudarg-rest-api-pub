package com.ayudarg.restapi.controller;

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

import com.ayudarg.restapi.model.Institution;
import com.ayudarg.restapi.model.Resource;
import com.ayudarg.restapi.model.Transaction;
import com.ayudarg.restapi.model.TransactionReview;
import com.ayudarg.restapi.model.enums.ResourceStatus;
import com.ayudarg.restapi.model.enums.TransactionStatus;
import com.ayudarg.restapi.payload.request.ReviewRequest;
import com.ayudarg.restapi.repository.InstitutionRepository;
import com.ayudarg.restapi.repository.ResourceRepository;
import com.ayudarg.restapi.repository.TransactionRepository;
import com.ayudarg.restapi.repository.UserRepository;
import com.ayudarg.restapi.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ResourceRepository resourceRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	
	@GetMapping("/accept/{id}")
	@PreAuthorize("isAuthenticated() and hasRole('USER')")
	public ResponseEntity<?> accept(@PathVariable Long id){
		try {
			
			UserDetailsImpl requestUser = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Optional<Transaction> transaction = transactionRepository.findById(id);
			
			if(transaction.isEmpty())
				return ResponseEntity.notFound().build();
			
			//Si el usuario no es admin de la institución, no puede aceptar
			if(!requestUser.getId().equals(((Institution)transaction.get().getDonation().getReceiver()).getAdmin().getId()))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no autorizado");
			
			//Si el donante canceló la transacción, no hay nada que el admin pueda hacer
			//Podría ser que el status Rejected sea reversible, sin embargo, esto llevaría a contemplar
			//nuevamente que la cantidad del recurso disponible sea suficiente y que el usuario esté
			//dispuesto a continuar donando. El mejor approach es que el status REJECTED sea final, sin embargo
			//de ser necesario, se puede buscar una alternativa.
			if(transaction.get().getStatus() == TransactionStatus.CANCELED || transaction.get().getStatus() == TransactionStatus.REJECTED)
				return ResponseEntity.badRequest().build();
			
			transaction.get().setStatus(TransactionStatus.ACCEPTED);
			
			return ResponseEntity.ok(transactionRepository.save(transaction.get()));
			
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/cancel/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> cancel(@PathVariable Long id){
		return null;
	}
	
	
	@GetMapping("/reject/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> reject(@PathVariable Long id){
		try {
			
			UserDetailsImpl requestUser = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Optional<Transaction> transaction = transactionRepository.findById(id);
			
			if(transaction.isEmpty())
				return ResponseEntity.notFound().build();
			
			//Si el usuario no es admin de la institución, no puede rechazar
			if(!requestUser.getId().equals(((Institution)transaction.get().getDonation().getReceiver()).getAdmin().getId()))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no autorizado");
			
			//Si el donante canceló la transacción, no hay nada que el admin pueda hacer
			//Podría ser que el status Rejected sea reversible, sin embargo, esto llevaría a contemplar
			//nuevamente que la cantidad del recurso disponible sea suficiente y que el usuario esté
			//dispuesto a continuar donando. El mejor approach es que el status REJECTED sea final, sin embargo
			//de ser necesario, se puede buscar una alternativa.
			if(transaction.get().getStatus() != TransactionStatus.PENDING)
				return ResponseEntity.badRequest().build();
			
			transaction.get().setStatus(TransactionStatus.REJECTED);
			//transaction.get().getResource().setStatus(ResourceStatus.DEAD);
			Resource resource = transaction.get().getResource();
			Resource parent = resource.getParent();
			
			resource.setStatus(ResourceStatus.DEAD);
			parent.setAmount(parent.getAmount()+resource.getAmount());
			
			return ResponseEntity.ok(transactionRepository.save(transaction.get()));
			
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/complete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> complete(@PathVariable Long id){
		try {
			
			UserDetailsImpl requestUser = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Optional<Transaction> transaction = transactionRepository.findById(id);
			
			if(transaction.isEmpty())
				return ResponseEntity.notFound().build();
			
			//Si el usuario no es admin de la institución, no puede confirmar
			if(!requestUser.getId().equals(((Institution)transaction.get().getDonation().getReceiver()).getAdmin().getId()))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no autorizado");
			
			//Si el donante canceló la transacción, no hay nada que el admin pueda hacer
			//Podría ser que el status Rejected sea reversible, sin embargo, esto llevaría a contemplar
			//nuevamente que la cantidad del recurso disponible sea suficiente y que el usuario esté
			//dispuesto a continuar donando. El mejor approach es que el status REJECTED sea final, sin embargo
			//de ser necesario, se puede buscar una alternativa.
			if(transaction.get().getStatus() != TransactionStatus.PENDING)
				return ResponseEntity.badRequest().build();
			
			transaction.get().setStatus(TransactionStatus.COMPLETED);
			//transaction.get().getResource().setStatus(ResourceStatus.DEAD);
			Resource resource = transaction.get().getResource();
			//Resource parent = resource.getParent();
			
			//El recurso pasa a estado ALIVE
			resource.setStatus(ResourceStatus.ALIVE);
			
			//El recurso tiene nuevo dueño: la institución
			resource.setOwner(transaction.get().getDonation().getReceiver());

			return ResponseEntity.ok(transactionRepository.save(transaction.get()));
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/review")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> review(@RequestBody ReviewRequest payload){
		try {
			
			UserDetailsImpl requestUser = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Optional<Transaction> transaction = transactionRepository.findById(payload.getTransaction_id());
			
			if(transaction.isEmpty())
				return ResponseEntity.notFound().build();
			
			//Si la transacción no se completó o no se rechazó, no puedo hacer un review
			if(transaction.get().getStatus() != TransactionStatus.COMPLETED && transaction.get().getStatus() != TransactionStatus.REJECTED)
				return ResponseEntity.badRequest().build();
			
			//Si el usuario no es admin de la institución, no puede hacer un review de la misma
			if(!requestUser.getId().equals(((Institution)transaction.get().getDonation().getReceiver()).getAdmin().getId()))
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no autorizado");
	
			TransactionReview review = new TransactionReview();
			
			review.setTitle(payload.getTitle());
			review.setRating(payload.getRating());
			review.setComment(payload.getComment());
			
			transaction.get().setReview(review);
			
			return ResponseEntity.ok(transactionRepository.save(transaction.get()));
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
			
}
