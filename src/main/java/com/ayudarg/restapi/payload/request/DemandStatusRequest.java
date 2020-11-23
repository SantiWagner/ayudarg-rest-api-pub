package com.ayudarg.restapi.payload.request;

import com.ayudarg.restapi.model.enums.DemandStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DemandStatusRequest {
	
	private DemandStatus status;
	
	private String comment;

}
