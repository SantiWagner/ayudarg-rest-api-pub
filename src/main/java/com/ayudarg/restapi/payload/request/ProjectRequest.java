package com.ayudarg.restapi.payload.request;

import java.util.List;

import com.ayudarg.restapi.model.Demand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectRequest {

	private Long institution_id;
	
	private String name;
	
	private String description;
	
	private List<Demand> demands;
	
}
