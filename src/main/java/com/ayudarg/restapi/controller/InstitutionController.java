package com.ayudarg.restapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ayudarg.restapi.model.Institution;
import com.ayudarg.restapi.model.User;
import com.ayudarg.restapi.repository.InstitutionRepository;
import com.ayudarg.restapi.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {
	
	@Autowired
	private InstitutionRepository institutionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/search")
	@PreAuthorize("hasRole('ADMIN') or hasRole('MOD') or hasRole('USER')")
	public ResponseEntity<Page<Institution>> getByName(
			@RequestParam String name,
			@RequestParam(defaultValue = "0") int page,
		    @RequestParam(defaultValue = "3") int size){
		
		Pageable paging = PageRequest.of(page, size);
		return ResponseEntity.ok(institutionRepository.findByNameContaining(name,paging));
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Institution> create(@RequestBody Institution institution){
		try {
			Institution toAdd = new Institution(institution.getName(), institution.getDescription());
			institutionRepository.save(toAdd);
			return new ResponseEntity<>(toAdd, HttpStatus.CREATED);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Institution> update(@RequestBody Institution institution){
		
		try {
			Optional<Institution> toUpdate = institutionRepository.findById(institution.getId());
			
			if(toUpdate.isPresent())
			{
				toUpdate.get().setName(institution.getName());
				toUpdate.get().setDescription(institution.getDescription());
				toUpdate.get().setAdmin(institution.getAdmin());
				return new ResponseEntity<>(institutionRepository.save(toUpdate.get()), HttpStatus.OK);
			}else {
				 return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	@PutMapping("/setadmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Institution> setAdmin(@RequestBody Institution institution){
		
		try {
			Optional<Institution> toUpdate = institutionRepository.findById(institution.getId());
			
			if(toUpdate.isPresent())
			{
				Optional<User> admin = userRepository.findById(institution.getAdmin().getId());
				
				if(admin.isPresent()) {
					 toUpdate.get().setAdmin(admin.get());
					 institutionRepository.save(toUpdate.get());
					 return new ResponseEntity<>(institutionRepository.save(toUpdate.get()), HttpStatus.OK);
				}else {
					 return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
				}
			}else {
				 return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
