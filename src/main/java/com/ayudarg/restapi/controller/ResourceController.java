package com.ayudarg.restapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayudarg.restapi.model.Resource;
import com.ayudarg.restapi.model.User;
import com.ayudarg.restapi.model.enums.ResourceStatus;
import com.ayudarg.restapi.repository.ResourceRepository;
import com.ayudarg.restapi.repository.UserRepository;
import com.ayudarg.restapi.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/resources")
public class ResourceController {
	
	@Autowired
	ResourceRepository resourceRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/details/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Resource> details(@PathVariable String id) {
		try {
			Optional<Resource> toReturn = resourceRepository.findById(Long.parseLong(id));
			
			if(toReturn.isEmpty()) {
				return ResponseEntity.notFound().build();
			}else {
				return ResponseEntity.ok(toReturn.get());
			}
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Resource> create(@RequestBody Resource resource) {
		try {
			UserDetailsImpl user = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Optional<User> owner = userRepository.findById(user.getId());
			Resource toAdd = new Resource();
			if(owner.isPresent()) {
				toAdd.setName(resource.getName());
				
				toAdd.setOwner(owner.get());
				toAdd.setAmount(resource.getAmount());
				toAdd.setMeasurement_unit(resource.getMeasurement_unit());
				toAdd.setStatus(ResourceStatus.ALIVE);
				
				return ResponseEntity.ok(resourceRepository.save(toAdd));
			}else {
				return ResponseEntity.badRequest().build();
			}
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity <Resource> update(@RequestBody Resource resource){
		try {
			
			Optional<Resource> toUpdateOptional = resourceRepository.findById(resource.getId());
			
			if(toUpdateOptional.isEmpty())
				return ResponseEntity.notFound().build();
			
			Resource toUpdate = toUpdateOptional.get();
			
			toUpdate.setAmount(resource.getAmount());
			toUpdate.setMeasurement_unit(resource.getMeasurement_unit());
			toUpdate.setName(resource.getName());
			toUpdate.setParent(resource.getParent());
			toUpdate.setStatus(resource.getStatus());
			
			return ResponseEntity.ok(resourceRepository.save(toUpdate));
			
		}catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	
	}
	
	

}
