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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayudarg.restapi.model.Demand;
import com.ayudarg.restapi.payload.request.DemandStatusRequest;
import com.ayudarg.restapi.repository.DemandRepository;
import com.ayudarg.restapi.repository.InstitutionRepository;
import com.ayudarg.restapi.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/demands")
public class DemandController {

	@Autowired
	DemandRepository demandRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@PutMapping("/setstatus/{id}")
	@PreAuthorize("isAuthenticated() and hasRole('USER')")
	public ResponseEntity<?> setStatus(@PathVariable Long id, @RequestBody DemandStatusRequest payload){
		
		try {
			
			UserDetailsImpl requestUser = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			
			Optional<Demand> demand = demandRepository.findById(id);
			
			if(demand.isEmpty())
				return ResponseEntity.notFound().build();
			
			//Si el usuario no es encargado de la institución, devuelvo BAD_REQUEST
			if(!requestUser.getId().equals(demand.get().getProject().getInstitution().getAdmin().getId()))
				return ResponseEntity.badRequest().build();
			
			demand.get().setStatus(payload.getStatus());
			demand.get().setStatus_comment(payload.getComment());
			
			return ResponseEntity.ok(demandRepository.save(demand.get()));
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/details/{id}")
	@PreAuthorize("isAuthenticated() and hasRole('USER')")
	public ResponseEntity<?> details(@PathVariable Long id){
		try {
			
			Optional<Demand> demand = demandRepository.findById(id);
			
			if(demand.isEmpty())
				return ResponseEntity.notFound().build();
			
			return ResponseEntity.ok(demand.get());
			
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
