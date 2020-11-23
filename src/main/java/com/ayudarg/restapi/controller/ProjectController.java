package com.ayudarg.restapi.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ayudarg.restapi.model.Demand;
import com.ayudarg.restapi.model.Institution;
import com.ayudarg.restapi.model.Project;
import com.ayudarg.restapi.model.enums.DemandStatus;
import com.ayudarg.restapi.payload.request.ProjectRequest;
import com.ayudarg.restapi.repository.InstitutionRepository;
import com.ayudarg.restapi.repository.ProjectRepository;
import com.ayudarg.restapi.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@GetMapping("/details/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Project> details(@PathVariable String id) {
		try {
			Optional<Project> toReturn = projectRepository.findById(Long.parseLong(id));
			
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
	public ResponseEntity<Project> create(@RequestBody ProjectRequest project){
		try {
			UserDetailsImpl requestUser = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Optional<Institution> institution = institutionRepository.findById(project.getInstitution_id());
			//Si el usuario que hace el request, no es admin de la institución, no puede crear proyectos
			if(!requestUser.getId().equals(institution.get().getAdmin().getId()))
				return ResponseEntity.badRequest().build();
			
			Project toAdd = new Project();
			
			Set<Demand> demandsToAdd = new HashSet<>();
			
			for(Demand d: project.getDemands()) {
				Demand newDemand = new Demand();
				newDemand.setDescription(d.getDescription());
				newDemand.setStatus(DemandStatus.UNSATISFIED);
				newDemand.setProject(toAdd);
				demandsToAdd.add(newDemand);
			}
			
			toAdd.setDemands(demandsToAdd);
			toAdd.setInstitution(institution.get());
			toAdd.setName(project.getName());
			toAdd.setDescription(project.getDescription());
			
			return ResponseEntity.ok(projectRepository.save(toAdd));
			
		}catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
}
